package common.util.lang;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import common.io.json.JsonClass;
import common.io.json.JsonField;
import common.pack.PackData.Identifier;
import common.util.pack.Background;

public class Formatter {

	@JsonClass
	public static class Context {

		public final String left = "(";
		public final String right = ")";
		public final String sqleft = "[";
		public final String sqright = "]";
		public final String crleft = "{";
		public final String crright = "}";

		@JsonField
		public boolean isEnemy;
		@JsonField
		public boolean useSecond;

		public Context() {
		}

		public Context(boolean ene, boolean sec) {
			isEnemy = ene;
			useSecond = sec;
		}

		public String abs(int v) {
			return "" + Math.abs(v);
		}

		public String bg(Identifier<Background> id) {
			return id.get().toString();
		}

		public String dispTime(int time) {
			if (useSecond)
				return toSecond(time) + "s";
			return time + "f";
		}

		public String entity(Identifier<?> id) {
			return id.get().toString();
		}

		public String toSecond(int time) {
			return "" + (time * 100 / 30 / 100.0);
		}

	}

	private class BoolElem extends Comp {

		public BoolElem(int start, int end) {
			super(start, end);
		}

		private boolean eval() throws Exception {
			for (int i = 0; i < MATCH.length; i++)
				for (int j = p0; j < p1 - MATCH[i].length() + 1; j++)
					if (test(i, j)) {
						int fi0 = new IntExp(p0, j).eval();
						int fi1 = new IntExp(j + MATCH[i].length(), p1).eval();
						if (i == 0)
							return fi0 >= fi1;
						if (i == 1)
							return fi0 <= fi1;
						if (i == 2)
							return fi0 == fi1;
						if (i == 3)
							return fi0 != fi1;
						if (i == 4)
							return fi0 > fi1;
						if (i == 5)
							return fi0 < fi1;
					}
			return (Boolean) new RefObj(p0, p1).eval();
		}

		private boolean test(int i, int j) {
			for (int k = 0; k < MATCH[i].length(); k++)
				if (str.charAt(j + k) != MATCH[i].charAt(k))
					return false;
			return true;
		}

	}

	private class BoolExp extends Comp {

		private int ind;

		public BoolExp(int start, int end) {
			super(start, end);
			ind = p0;
		}

		private boolean eval() throws Exception {
			Stack<Boolean> stack = new Stack<>();
			stack.push(nextElem());
			while (ind < p1) {
				char ch = str.charAt(ind++);
				if (ch == '&')
					stack.push(stack.pop() & nextElem());
				else if (ch == '|')
					stack.push(nextElem());
				else
					throw new Exception("unknown operator " + ch + " at " + (ind - 1));
			}
			boolean b = false;
			for (boolean bi : stack)
				b |= bi;
			return b;
		}

		private boolean nextElem() throws Exception {
			char ch = str.charAt(ind);
			boolean neg = ch == '!';
			if (neg)
				ch = str.charAt(++ind);
			if (ch == '!')
				throw new Exception("double ! at " + ind);
			if (ch == '(') {
				int depth = 1;
				int pre = ++ind;
				while (depth > 0) {
					char chr = str.charAt(ind++);
					if (chr == '(')
						depth++;
					if (chr == ')')
						depth--;
				}
				return neg ^ new BoolExp(pre, ind - 1).eval();
			}
			int pre = ind;
			while (ch != '&' && ch != '|' && ind < p1)
				ch = str.charAt(++ind);
			return neg ^ new BoolElem(pre, ind).eval();
		}
	}

	private class Code implements IElem {

		private BoolExp cond;
		private Root data;

		private Code(BoolExp c, Root d) {
			cond = c;
			data = d;
		}

		@Override
		public void build(StringBuilder sb) throws Exception {
			if (cond.eval())
				data.build(sb);
		}

	}

	private class CodeBlock extends Cont {

		private CodeBlock(int start, int end) throws Exception {
			super(start, end);
			int i = p0;
			while (i < p1) {
				char ch = str.charAt(i++);
				if (ch == '(') {
					int depth = 1;
					int pre = i;
					while (depth > 0) {
						if (i >= p1)
							throw new Exception("unfinished at " + i);
						char chr = str.charAt(i++);
						if (chr == '(')
							depth++;
						if (chr == ')')
							depth--;
					}
					BoolExp cond = new BoolExp(pre, i - 1);
					while (str.charAt(i++) != '{')
						if (i >= p1)
							throw new Exception("unfinished at " + i);
					depth = 1;
					pre = i;
					while (depth > 0) {
						if (i >= p1)
							throw new Exception("unfinished at " + i);
						char chr = str.charAt(i++);
						if (chr == '{')
							depth++;
						if (chr == '}')
							depth--;
					}
					Root data = new Root(pre, i - 1);
					list.add(new Code(cond, data));
				}
			}
		}

	}

	private abstract class Comp {

		public final int p0, p1;

		public Comp(int start, int end) {
			p0 = start;
			p1 = end;
		}
	}

	private abstract class Cont extends Elem {

		public final List<IElem> list = new ArrayList<>();

		public Cont(int start, int end) {
			super(start, end);
		}

		@Override
		public void build(StringBuilder sb) throws Exception {
			for (IElem e : list)
				e.build(sb);
		}

	}

	private abstract class Elem extends Comp implements IElem {

		public Elem(int start, int end) {
			super(start, end);
		}

	}

	private interface IElem {

		public void build(StringBuilder sb) throws Exception;

	}

	private class IntExp extends Comp {

		private int ind;

		public IntExp(int start, int end) {
			super(start, end);
			ind = p0;
		}

		private int eval() throws Exception {
			Stack<Integer> stack = new Stack<>();
			char opera = '\0';
			stack.push(nextElem());
			while (ind < p1) {
				char ch = str.charAt(ind++);
				if (ch == '*')
					stack.push(stack.pop() * nextElem());
				else if (ch == '/')
					stack.push(stack.pop() / nextElem());
				else if (ch == '%')
					stack.push(stack.pop() % nextElem());
				else if (ch == '+' || ch == '-') {
					if (opera != ' ') {
						int b = stack.pop();
						int a = stack.pop();
						if (opera == '+')
							stack.push(a + b);
						else
							stack.push(a - b);
					}
					stack.push(nextElem());
					opera = ch;
				} else
					throw new Exception("unknown operator " + ch + " at " + (ind - 1));
			}
			if (opera != '\0') {
				int b = stack.pop();
				int a = stack.pop();
				if (opera == '+')
					stack.push(a + b);
				else
					stack.push(a - b);
			}
			return stack.pop();
		}

		private int nextElem() throws Exception {
			char ch = str.charAt(ind);
			int neg = 1;
			if (ch == '-') {
				neg = -1;
				ch = str.charAt(++ind);
			}
			if (ch == '(') {
				int depth = 1;
				int pre = ++ind;
				while (depth > 0) {
					char chr = str.charAt(ind++);
					if (chr == '(')
						depth++;
					if (chr == ')')
						depth--;
				}
				return neg * new IntExp(pre, ind - 1).eval();
			}
			if (ch >= '0' && ch <= '9')
				return neg * readNumber();
			int pre = ind;
			while (ch != '+' && ch != '-' && ch != '*' && ch != '/' && ch != '%' && ind < p1)
				ch = str.charAt(++ind);
			return neg * (Integer) new RefObj(pre, ind).eval();
		}

		private int readNumber() throws Exception {
			int ans = 0;
			while (ind < p1) {
				char chr = str.charAt(ind);
				if (chr < '0' || chr > '9')
					break;
				ind++;
				ans = ans * 10 + chr - '0';
			}
			return ans;
		}

	}

	private abstract class RefElem extends Comp {

		public RefElem(int start, int end) {
			super(start, end);
		}

		public abstract Object eval(Object parent) throws Exception;
	}

	private class RefField extends RefElem {

		public RefField(int start, int end) {
			super(start, end);
		}

		@Override
		public Object eval(Object parent) throws Exception {
			if (str.charAt(p0) == '_' && parent != null)
				throw new Exception("global only allowed for bottom level");
			if (parent == null)
				if (str.charAt(p0) == '_')
					parent = ctx;
				else
					parent = obj;
			String name = str.substring(str.charAt(p0) == '_' ? p0 + 1 : p0, p1);
			Field f = parent.getClass().getField(name);
			return f.get(parent);
		}
	}

	private class RefFunc extends RefElem {

		private List<RefObj> list = new ArrayList<>();

		public RefFunc(int start, int end) {
			super(start, end);
		}

		@Override
		public Object eval(Object parent) throws Exception {
			if (str.charAt(p0) == '_' && parent != null)
				throw new Exception("global only allowed for bottom level: at " + p0);
			if (parent == null)
				if (str.charAt(p0) == '_')
					parent = ctx;
				else
					parent = obj;
			String name = str.substring(str.charAt(p0) == '_' ? p0 + 1 : p0, p1);
			Method[] ms = parent.getClass().getMethods();
			Object[] args = new Object[list.size()];
			for (int i = 0; i < args.length; i++)
				args[i] = list.get(i).eval();
			for (Method m : ms)
				if (m.getName().equals(name) && m.getParameterCount() == list.size())
					return m.invoke(parent, args);
			throw new Exception("function " + name + " not found for class " + parent.getClass());
		}
	}

	private class RefObj extends Elem {

		private List<RefElem> list = new ArrayList<>();

		private RefObj(int start, int end) throws Exception {
			super(start, end);
			int pre = p0, i = p0;
			while (i < p1) {
				char ch = str.charAt(i++);
				if (ch == '.') {
					list.add(new RefField(pre, i - 1));
					pre = i;
				}
				if (ch == '(') {
					RefFunc func = new RefFunc(pre, i - 1);
					pre = i;
					int depth = 1;
					while (depth > 0) {
						if (i >= p1)
							throw new Exception("unfinished at " + i);
						char chr = str.charAt(i++);
						if (chr == '(')
							depth++;
						if (chr == ')') {
							depth--;
							if (depth == 0) {
								if (i - 1 > pre)
									func.list.add(new RefObj(pre, i - 1));
								pre = i;
							}
						}
						if (chr == ',' && depth == 1) {
							func.list.add(new RefObj(pre, i - 1));
							pre = i;
						}
					}
					list.add(func);
				}
			}
			if (pre < p1)
				list.add(new RefField(pre, p1));
		}

		@Override
		public void build(StringBuilder sb) throws Exception {
			sb.append("" + eval());
		}

		private Object eval() throws Exception {
			Object obj = null;
			for (RefElem e : list)
				obj = e.eval(obj);
			return obj;
		}

	}

	private class Root extends Cont {

		private Root(int start, int end) throws Exception {
			super(start, end);
			int pre = p0;
			int deepth = 0;
			for (int i = p0; i < p1; i++) {
				char ch = str.charAt(i);
				if (ch == '[') {
					if (deepth == 0 && i > pre) {
						list.add(new TextRef(pre, i));
						pre = i + 1;
					}
					deepth++;
				}
				if (ch == ']') {
					deepth--;
					if (deepth == 0 && i > pre) {
						list.add(new CodeBlock(pre, i));
						pre = i + 1;
					}
				}
			}
			if (pre < p1)
				list.add(new TextRef(pre, p1));
		}

	}

	private class TextPlain extends Elem {

		private TextPlain(int start, int end) {
			super(start, end);
		}

		@Override
		public void build(StringBuilder sb) {
			sb.append(str, p0, p1);
		}

	}

	private class TextRef extends Cont {

		private TextRef(int start, int end) throws Exception {
			super(start, end);
			int pre = p0;
			int depth = 0;
			for (int i = p0; i < p1; i++) {
				char ch = str.charAt(i);
				if (ch == '(') {
					if (depth == 0 && i > pre)
						list.add(new TextPlain(pre, i));
					if (depth == 0)
						pre = i + 1;
					depth++;
				}
				if (ch == ')') {
					depth--;
					if (depth == 0 && i > pre)
						list.add(new RefObj(pre, i));
					if (depth == 0)
						pre = i + 1;
				}
			}
			if (pre < p1)
				list.add(new TextPlain(pre, p1));
		}

	}

	private static final String[] MATCH = { ">=", "<=", "==", "!=", ">", "<" };

	public static String format(String pattern, Object obj, Object ctx) {
		StringBuilder sb = new StringBuilder();
		try {
			Formatter f = new Formatter(pattern, obj, ctx);
			f.root.build(sb);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	private final String str;
	private final Object obj;
	private final Object ctx;
	private final Root root;

	private Formatter(String pattern, Object object, Object context) throws Exception {
		str = pattern;
		obj = object;
		ctx = context;
		String err = check();
		if (err != null)
			throw new Exception(err);
		root = new Root(0, str.length());
	}

	private String check() {
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < str.length(); i++) {
			char ch = str.charAt(i);
			if (ch == '(')
				stack.push(0);
			if (ch == '[')
				stack.push(1);
			if (ch == '{')
				stack.push(2);
			if (ch == ')' && (stack.isEmpty() || stack.pop() != 0))
				return "bracket ) unexpected at " + i;
			if (ch == ']' && (stack.isEmpty() || stack.pop() != 1))
				return "bracket ] unexpected at " + i;
			if (ch == '}' && (stack.isEmpty() || stack.pop() != 2))
				return "bracket } unexpected at " + i;
		}
		return stack.isEmpty() ? null : "unenclosed bracket: " + stack;
	}

}
