package common.battle.entity;

import common.CommonStatic;
import common.CommonStatic.BattleConst;
import common.battle.StageBasis;
import common.battle.attack.*;
import common.battle.data.AtkDataModel;
import common.battle.data.MaskEntity;
import common.battle.data.PCoin;
import common.pack.Identifier;
import common.pack.UserProfile;
import common.system.P;
import common.system.fake.FakeGraphics;
import common.system.fake.FakeTransform;
import common.util.BattleObj;
import common.util.Data;
import common.util.Data.Proc.POISON;
import common.util.Data.Proc.REVIVE;
import common.util.anim.AnimU.UType;
import common.util.anim.EAnimD;
import common.util.anim.EAnimU;
import common.util.anim.MaModel;
import common.util.pack.DemonSoul;
import common.util.pack.EffAnim;
import common.util.pack.EffAnim.*;
import common.util.pack.Soul;
import common.util.pack.Soul.SoulType;
import common.util.unit.Level;

import java.util.*;

/**
 * Entity class for units and enemies
 */
@SuppressWarnings("ForLoopReplaceableByForEach")
public abstract class Entity extends AbEntity {

	public static class AnimManager extends BattleObj {

		private final Entity e;
		private final int[][] status;

		/**
		 * dead FSM time <br>
		 * -1 means not dead<br>
		 * positive value means time remain for death anim to play
		 */
		public int dead = -1;

		/**
		 * KB anim, null means not being KBed, can have various value during battle
		 */
		private EAnimD<KBEff> back;

		/**
		 * entity anim
		 */
		private final EAnimU anim;

		/**
		 * corpse anim
		 */
		private EAnimD<ZombieEff> corpse;

		/**
		 * soul anim, null means not dead yet
		 */
		private EAnimD<?> soul;

		/**
		 * smoke animation for each entity
		 */
		public EAnimD<DefEff> smoke;

		/**
		 * Layer for smoke animation
		 */
		public int smokeLayer;

		/**
		 * x-pos of smoke animation
		 */
		public int smokeX;

		/**
		 * responsive effect FSM time
		 */
		private int efft;

		/**
		 * responsive effect FSM type
		 */
		private int eftp;

		/**
		 * on-entity effect icons<br>
		 * index defined by Data.A_()
		 */
		private final EAnimD<?>[] effs = new EAnimD[A_TOT];

		private AnimManager(Entity ent, EAnimU ea) {
			e = ent;
			anim = ea;
			status = e.status;
		}

		protected MaModel getMaModel() {
			return anim.anim().mamodel;
		}

		/**
		 * draw this entity
		 */
		public void draw(FakeGraphics gra, P p, double siz) {
			if (dead > 0) {
				//100 is guessed value comparing from BC
				p.y -= 100 * siz;
				soul.draw(gra, p, siz);
				return;
			}
			FakeTransform at = gra.getTransform();
			if (corpse != null)
				corpse.draw(gra, p, siz);
			if (corpse == null || status[P_REVIVE][1] < Data.REVIVE_SHOW_TIME) {
				if (corpse != null) {
					gra.setTransform(at);
					anim.changeAnim(UType.IDLE, false);
				}
			} else {
				gra.delete(at);
				return;
			}

			anim.paraTo(back);
			if (e.kbTime == 0 || e.kb.kbType != INT_WARP)
				anim.draw(gra, p, siz);
			anim.paraTo(null);
			gra.setTransform(at);
			if (CommonStatic.getConfig().ref)
				e.drawAxis(gra, p, siz);
			gra.delete(at);
		}

		/**
		 * draw the effect icons
		 */
		public void drawEff(FakeGraphics g, P p, double siz) {
			if (dead != -1)
				return;
			if (status[P_WARP][2] != 0)
				return;

			FakeTransform at = g.getTransform();
			int EWID = 36;
			double x = p.x;
			if (effs[eftp] != null) {
				effs[eftp].draw(g, p, siz * 0.75);
			}

			for(int i = 0; i < effs.length; i++) {
				if(i == A_B || i == A_E_B || i == A_DEMON_SHIELD || i == A_E_DEMON_SHIELD)
					continue;

				EAnimD<?> eae = effs[i];

				if (eae == null)
					continue;

				double offset = 0.0;

				g.setTransform(at);
				eae.draw(g, new P(x, p.y+offset), siz * 0.75);
				x -= EWID * e.dire * siz;
			}

			x = p.x;

			for(int i = 0; i < effs.length; i++) {
				if(i == A_B || i == A_E_B || i == A_DEMON_SHIELD || i == A_E_DEMON_SHIELD) {
					EAnimD<?> eae = effs[i];

					if(eae == null)
						continue;

					double offset = -25.0 * siz;

					g.setTransform(at);

					eae.draw(g, new P(x, p.y + offset), siz * 0.75);
				}
			}

			g.delete(at);
		}

		/**
		 * get a effect icon
		 */
		@SuppressWarnings("unchecked")
		public void getEff(int t) {
			int dire = e.dire;
			if (t == INV) {
				effs[eftp] = null;
				eftp = A_EFF_INV;
				effs[eftp] = effas().A_EFF_INV.getEAnim(DefEff.DEF);
				efft = effas().A_EFF_INV.len(DefEff.DEF);
			}
			if (t == P_WAVE) {
				int id = dire == -1 ? A_WAVE_INVALID : A_E_WAVE_INVALID;
				EffAnim<DefEff> eff = dire == -1 ? effas().A_WAVE_INVALID : effas().A_E_WAVE_INVALID;
				effs[id] = eff.getEAnim(DefEff.DEF);
				status[P_WAVE][0] = eff.len(DefEff.DEF);
			}
			if (t == STPWAVE) {
				effs[eftp] = null;
				eftp = dire == -1 ? A_WAVE_STOP : A_E_WAVE_STOP;
				EffAnim<DefEff> eff = dire == -1 ? effas().A_WAVE_STOP : effas().A_E_WAVE_STOP;
				effs[eftp] = eff.getEAnim(DefEff.DEF);
				efft = eff.len(DefEff.DEF);
			}
			if (t == INVWARP) {
				effs[eftp] = null;
				eftp = dire == -1 ? A_FARATTACK : A_E_FARATTACK;
				EffAnim<DefEff> eff = dire == -1 ? effas().A_FARATTACK : effas().A_E_FARATTACK;
				effs[eftp] = eff.getEAnim(DefEff.DEF);
				efft = eff.len(DefEff.DEF);
			}
			if (t == P_STOP) {
				int id = dire == -1 ? A_STOP : A_E_STOP;
				effs[id] = (dire == -1 ? effas().A_STOP : effas().A_E_STOP).getEAnim(DefEff.DEF);
			}
			if (t == P_IMUATK) {
				effs[A_IMUATK] = effas().A_IMUATK.getEAnim(DefEff.DEF);
			}
			if (t == P_SLOW) {
				int id = dire == -1 ? A_SLOW : A_E_SLOW;
				effs[id] = (dire == -1 ? effas().A_SLOW : effas().A_E_SLOW).getEAnim(DefEff.DEF);
			}
			if (t == P_WEAK) {
				if (status[P_WEAK][1] <= 100) {
					int id = dire == -1 ? A_DOWN : A_E_DOWN;
					effs[id] = (dire == -1 ? effas().A_DOWN : effas().A_E_DOWN).getEAnim(DefEff.DEF);
				} else {
					int id = dire == -1 ? A_WEAK_UP : A_E_WEAK_UP;
					effs[id] = (dire == -1 ? effas().A_WEAK_UP : effas().A_E_WEAK_UP).getEAnim(WeakUpEff.UP);
				}
			}
			if (t == P_CURSE) {
				int id = dire == -1 ? A_CURSE : A_E_CURSE;
				effs[id] = (dire == -1 ? effas().A_CURSE : effas().A_E_CURSE).getEAnim(DefEff.DEF);
			}
			if (t == P_POISON) {
				int mask = status[P_POISON][0];
				EffAnim<?>[] arr = { effas().A_POI0, e.dire == -1 ? effas().A_POI1 : effas().A_POI1_E, effas().A_POI2, effas().A_POI3, effas().A_POI4,
						effas().A_POI5, effas().A_POI6, effas().A_POI7 };
				for (int i = 0; i < A_POIS.length; i++)
					if ((mask & (1 << i)) > 0) {
						int id = A_POIS[i];
						effs[id] = ((EffAnim<DefEff>) arr[i]).getEAnim(DefEff.DEF);
					}

			}
			if (t == P_SEAL) {
				effs[A_SEAL] = effas().A_SEAL.getEAnim(DefEff.DEF);
			}
			if (t == P_STRONG) {
				int id = dire == -1 ? A_UP : A_E_UP;
				effs[id] = (dire == -1 ? effas().A_UP : effas().A_E_UP).getEAnim(DefEff.DEF);
			}
			if (t == P_LETHAL) {
				int id = dire == -1 ? A_SHIELD : A_E_SHIELD;
				EffAnim<DefEff> ea = dire == -1 ? effas().A_SHIELD : effas().A_E_SHIELD;
				status[P_LETHAL][1] = ea.len(DefEff.DEF);
				effs[id] = ea.getEAnim(DefEff.DEF);
				CommonStatic.setSE(SE_LETHAL);
			}
			if (t == P_WARP) {
				EffAnim<WarpEff> ea = effas().A_W;
				int ind = status[P_WARP][2];
				WarpEff pa = ind == 0 ? WarpEff.ENTER : WarpEff.EXIT;
				e.basis.lea.add(new WaprCont(e.pos, pa, e.layer, anim, e.dire));
				e.basis.lea.sort(Comparator.comparingInt(e -> e.layer));
				CommonStatic.setSE(ind == 0 ? SE_WARP_ENTER : SE_WARP_EXIT);
				status[P_WARP][ind] = ea.len(pa);

			}

			if (t == BREAK_ABI) {
				int id = dire == -1 ? A_B : A_E_B;
				effs[id] = (dire == -1 ? effas().A_B : effas().A_E_B).getEAnim(BarrierEff.BREAK);
				status[P_BREAK][0] = effs[id].len();
				CommonStatic.setSE(SE_BARRIER_ABI);
			}
			if (t == BREAK_ATK) {
				int id = dire == -1 ? A_B : A_E_B;
				effs[id] = (dire == -1 ? effas().A_B : effas().A_E_B).getEAnim(BarrierEff.DESTR);
				status[P_BREAK][0] = effs[id].len();
				CommonStatic.setSE(SE_BARRIER_ATK);
			}
			if (t == BREAK_NON) {
				int id = dire == -1 ? A_B : A_E_B;
				effs[id] = (dire == -1 ? effas().A_B : effas().A_E_B).getEAnim(BarrierEff.NONE);
				status[P_BREAK][0] = effs[id].len();
				CommonStatic.setSE(SE_BARRIER_NON);
			}
			if (t == P_ARMOR) {
				int id = dire == -1 ? A_ARMOR : A_E_ARMOR;
				EffAnim<ArmorEff> eff = dire == -1 ? effas().A_ARMOR : effas().A_E_ARMOR;
				ArmorEff index = status[P_ARMOR][1] >= 0 ? ArmorEff.DEBUFF : ArmorEff.BUFF;
				effs[id] = eff.getEAnim(index);
			}

			if (t == P_SPEED) {
				int id = dire == -1 ? A_SPEED : A_E_SPEED;
				EffAnim<SpeedEff> eff = dire == -1 ? effas().A_SPEED : effas().A_E_SPEED;
				SpeedEff index;

				if (status[P_SPEED][2] <= 1) {
					index = status[P_SPEED][1] >= 0 ? SpeedEff.UP : SpeedEff.DOWN;
				} else {
					index = status[P_SPEED][1] >= e.data.getSpeed() ? SpeedEff.UP : SpeedEff.DOWN;
				}

				effs[id] = eff.getEAnim(index);
			}

			if (t == HEAL) {
				EffAnim<DefEff> eff = dire == -1 ? effas().A_HEAL : effas().A_E_HEAL;

				effs[dire == -1 ? A_HEAL : A_E_HEAL] = eff.getEAnim(DefEff.DEF);
			}

			if (t == SHIELD_HIT) {
				int id = dire == -1 ? A_DEMON_SHIELD : A_E_DEMON_SHIELD;

				EffAnim<ShieldEff> eff = dire == -1 ? effas().A_DEMON_SHIELD : effas().A_E_DEMON_SHIELD;

				boolean half = e.currentShield * 1.0 / e.getProc().DEMONSHIELD.hp < 0.5;

				effs[id] = eff.getEAnim(half ? ShieldEff.HALF : ShieldEff.FULL);
				status[P_DEMONSHIELD][0] = effs[id].len();

				CommonStatic.setSE(SE_SHIELD_HIT);
			}

			if (t == SHIELD_BROKEN) {
				int id = dire == -1 ? A_DEMON_SHIELD : A_E_DEMON_SHIELD;

				EffAnim<ShieldEff> eff = dire == -1 ? effas().A_DEMON_SHIELD : effas().A_E_DEMON_SHIELD;

				effs[id] = eff.getEAnim(ShieldEff.BROKEN);
				status[P_DEMONSHIELD][0] = effs[id].len();

				CommonStatic.setSE(SE_SHIELD_BROKEN);
			}

			if (t == SHIELD_REGEN) {
				int id = dire == -1 ? A_DEMON_SHIELD : A_E_DEMON_SHIELD;

				EffAnim<ShieldEff> eff = dire == -1 ? effas().A_DEMON_SHIELD : effas().A_E_DEMON_SHIELD;

				effs[id] = eff.getEAnim(ShieldEff.REGENERATION);
				status[P_DEMONSHIELD][0] = effs[id].len();

				CommonStatic.setSE(SE_SHIELD_REGEN);
			}

			if (t == SHIELD_BREAKER) {
				int id = dire == -1 ? A_DEMON_SHIELD : A_E_DEMON_SHIELD;

				EffAnim<ShieldEff> eff = dire == -1 ? effas().A_DEMON_SHIELD : effas().A_E_DEMON_SHIELD;

				effs[id] = eff.getEAnim(ShieldEff.BREAKER);
				status[P_DEMONSHIELD][0] = effs[id].len();

				CommonStatic.setSE(SE_SHIELD_BREAKER);
			}
		}

		/**
		 * update effect icons animation
		 */
		private void checkEff() {
			int dire = e.dire;
			if (efft == 0)
				effs[eftp] = null;
			if (status[P_STOP][0] == 0) {
				int id = dire == -1 ? A_STOP : A_E_STOP;
				effs[id] = null;
			}
			if (status[P_SLOW][0] == 0) {
				int id = dire == -1 ? A_SLOW : A_E_SLOW;
				effs[id] = null;
			}
			if (status[P_WEAK][0] == 0) {
				int id;

				if (status[P_WEAK][1] <= 100) {
					id = dire == -1 ? A_DOWN : A_E_DOWN;
				} else {
					id = dire == -1 ? A_WEAK_UP : A_E_WEAK_UP;
				}

				effs[id] = null;
			}
			if (status[P_CURSE][0] == 0) {
				int id = dire == -1 ? A_CURSE : A_E_CURSE;
				effs[id] = null;
			}
			if (status[P_IMUATK][0] == 0) {
				effs[A_IMUATK] = null;
			}
			if (status[P_POISON][0] == 0) {
				effs[A_POI0] = null;
			}
			if (status[P_SEAL][0] == 0) {
				effs[A_SEAL] = null;
			}
			if (status[P_LETHAL][1] == 0) {
				int id = dire == -1 ? A_SHIELD : A_E_SHIELD;
				effs[id] = null;
			} else
				status[P_LETHAL][1]--;
			if (status[P_WAVE][0] == 0) {
				int id = dire == -1 ? A_WAVE_INVALID : A_E_WAVE_INVALID;
				effs[id] = null;
			} else
				status[P_WAVE][0]--;
			if (status[P_STRONG][0] == 0) {
				int id = dire == -1 ? A_UP : A_E_UP;
				effs[id] = null;
			}
			if (status[P_BREAK][0] == 0) {
				int id = dire == -1 ? A_B : A_E_B;
				effs[id] = null;
			} else
				status[P_BREAK][0]--;

			if (status[P_ARMOR][0] == 0) {
				int id = dire == -1 ? A_ARMOR : A_E_ARMOR;
				effs[id] = null;
			}

			if (status[P_SPEED][0] == 0) {
				int id = dire == -1 ? A_SPEED : A_E_SPEED;
				effs[id] = null;
			}

			if(effs[A_HEAL] != null) {
				if(effs[A_HEAL].done())
					effs[A_HEAL] = null;
			}

			efft--;
		}

		/**
		 * process kb animation <br>
		 * called when kb is applied
		 */
		private void kbAnim() {
			int t = e.kb.kbType;
			if (t != INT_SW && t != INT_WARP)
				setAnim(UType.HB, true);
			else
				setAnim(UType.WALK, false);
			if (t == INT_WARP) {
				e.kbTime = status[P_WARP][0];
				getEff(P_WARP);
				status[P_WARP][2] = 1;
			}
			if (t == INT_KB)
				e.kbTime = status[P_KB][0];
			if (t == INT_HB)
				back = effas().A_KB.getEAnim(KBEff.KB);
			if (t == INT_SW)
				back = effas().A_KB.getEAnim(KBEff.SW);
			if (t == INT_ASS)
				back = effas().A_KB.getEAnim(KBEff.ASS);
			if (t != INT_WARP)
				e.kbTime += 1;

			// Z-kill icon
			if (e.health <= 0 && e.zx.tempZK && (e.type & TB_ZOMBIE) != 0) {
				EAnimD<DefEff> eae = effas().A_Z_STRONG.getEAnim(DefEff.DEF);
				e.basis.lea.add(new EAnimCont(e.pos, e.layer, eae));
				e.basis.lea.sort(Comparator.comparingInt(e -> e.layer));
				CommonStatic.setSE(SE_ZKILL);
			}
		}

		private boolean deathSurge = false;

		/**
		 * set kill anim
		 */
		private void kill() {
			if ((e.getAbi() & AB_GLASS) != 0) {
				dead = 0;
				return;
			}

			if (e.getProc().DEATHSURGE.exists() && e.getProc().DEATHSURGE.perform(e.basis.r)) {
				deathSurge = true;
				soul = UserProfile.getBCData().demonSouls.get((1 - e.dire) / 2).getEAnim(DemonSoul.DemonSoulType.DEF);
				dead = soul.len();
				CommonStatic.setSE(SE_DEATH_SURGE);
			} else {
				Soul s = Identifier.get(e.data.getDeathAnim());
				dead = s == null ? 0 : (soul = s.getEAnim(SoulType.DEF)).len();
			}
		}

		private int setAnim(UType t, boolean skip) {
			if (anim.type != t)
				anim.changeAnim(t, skip);
			return anim.len();
		}

		private void update() {
			checkEff();

			for (int i = 0; i < effs.length; i++)
				if (effs[i] != null)
					effs[i].update(false);

			boolean checkKB = e.kb.kbType != INT_SW && e.kb.kbType != INT_WARP;
			if (status[P_STOP][0] == 0 && (e.kbTime == 0 || checkKB))
				anim.update(false);
			if (back != null)
				back.update(false);
			if (dead > 0) {
				soul.update(false);
				dead--;
			}
			if (anim.done() && anim.type == UType.ENTER)
				setAnim(UType.IDLE, true);
			if (dead >= 0) {
				if (deathSurge && soul.len() - dead == 21) //21 is guessed delay compared to BC
					e.aam.getDeathSurge();
				if (e.data.getResurrection() != null) {
					AtkDataModel adm = e.data.getResurrection();
					if (soul == null || adm.pre == soul.len() - dead)
						e.basis.getAttack(e.aam.getAttack(e.data.getAtkCount() + 1));
				}
			}
			if(smoke != null) {
				if(smoke.done()) {
					smoke = null;
					smokeLayer = -1;
					smokeX = -1;
				} else {
					smoke.update(false);
				}
			}
		}

	}

	private static class AtkManager extends BattleObj {

		/**
		 * atk FSM time
		 */
		private int atkTime;

		/**
		 * attack times remain
		 */
		private int loop;

		/**
		 * atk id primarily for display
		 */
		private int tempAtk = -1;

		private final Entity e;

		/**
		 * const field, attack count
		 */
		private final int multi;

		/**
		 * atk loop FSM type
		 */
		private int preID;

		/**
		 * pre-atk time const field
		 */
		private final int[] pres;

		/**
		 * atk loop FSM time
		 */
		private int preTime;

		private AtkManager(Entity ent) {
			e = ent;
			int[][] raw = e.data.rawAtkData();
			pres = new int[multi = raw.length];
			for (int i = 0; i < multi; i++)
				pres[i] = raw[i][1];
			loop = e.data.getAtkLoop();
		}

		private void setUp() {
			atkTime = e.data.getAnimLen();
			preID = 0;
			preTime = pres[0] - 1;
			e.anim.setAnim(UType.ATK, true);
		}

		private void stopAtk() {
			if (atkTime > 0)
				atkTime = preTime = 0;
		}

		/**
		 * update attack state
		 */
		private void updateAttack() {
			atkTime--;
			if (preTime >= 0) {
				if (preTime == 0) {
					int atk0 = preID;
					while (++preID < multi && pres[preID] == 0)
						;
					tempAtk = (int) (atk0 + e.basis.r.nextDouble() * (preID - atk0));
					e.basis.getAttack(e.aam.getAttack(tempAtk));
					if (preID < multi) {
						preTime = pres[preID];
					} else {
						loop--;
						e.waitTime = Math.max(e.data.getTBA() - 1, 0);
					}
				}
				preTime--;
			}
			if (atkTime == 0) {
				e.canBurrow = true;
				e.anim.setAnim(UType.IDLE, true);
			}
		}
	}

	private static class KBManager extends BattleObj {

		/**
		 * KB FSM type
		 */
		private int kbType;

		private final Entity e;

		/**
		 * remaining distance to KB
		 */
		private double kbDis;

		/**
		 * temp field to store wanted KB length
		 */
		private double tempKBdist;

		/**
		 * temp field to store wanted KB type
		 */
		private int tempKBtype = -1;

		private double initPos;
		private double kbDuration;
		private double time = 1;

		private KBManager(Entity ent) {
			e = ent;
		}

		/**
		 * process the interruption received
		 */
		private void doInterrupt() {
			int t = tempKBtype;
			if (t == -1)
				return;
			double d = tempKBdist;
			tempKBtype = -1;
			e.clearState();
			kbType = t;
			e.kbTime = KB_TIME[t];
			kbDis = d;
			initPos = e.pos;
			kbDuration = e.kbTime;
			time = 1;
			e.anim.kbAnim();
		}

		private double easeOut(double time, double start, double end, double duration, double dire) {
			time /= duration;
			return -end * time * (time - 2) * dire + start;
		}

		private void interrupt(int t, double d) {
			if (t == INT_ASS && (e.getAbi() & AB_SNIPERI) > 0) {
				e.anim.getEff(INV);
				return;
			}
			if (t == INT_SW && (e.getAbi() & AB_IMUSW) > 0) {
				e.anim.getEff(INV);
				return;
			}
			int prev = tempKBtype;
			if (prev == -1 || KB_PRI[t] >= KB_PRI[prev]) {
				tempKBtype = t;
				tempKBdist = d;
			}
		}

		private void kbmove(double mov) {
			if (mov < 0)
				e.updateMove(-mov, -mov);
			else {
				if(kbType == INT_WARP) {
					e.pos -= mov * e.dire;
				} else {
					double lim = e.getLim();
					e.pos -= Math.min(mov, lim) * e.dire;
				}
			}
		}

		/**
		 * update KB state <br>
		 * in KB state: deal with warp, KB go back, and anim change <br>
		 * end of KB: check whether it's killed, deal with revive
		 */
		private void updateKB() {
			e.kbTime--;
			if (e.kbTime == 0) {
				if(e.isBase) {
					e.anim.setAnim(UType.HB, false);
					return;
				}

				e.anim.back = null;
				e.anim.setAnim(UType.WALK, true);

				kbDuration = 0;
				initPos = 0;
				time = 1;

				if(kbType == INT_HB && e.health > 0 && e.getProc().DEMONSHIELD.hp > 0) {
					e.currentShield = (int) (e.getProc().DEMONSHIELD.hp * e.getProc().DEMONSHIELD.regen * e.shieldMagnification / 100.0);

					e.anim.getEff(SHIELD_REGEN);
				}

				if (e.health <= 0)
					e.preKill();
			} else {
				if (kbType != INT_WARP && kbType != INT_KB) {
					double mov = kbDis / e.kbTime;
					kbDis -= mov;
					kbmove(mov);
				} else if (kbType == INT_KB) {
					if (time == 1) {
						kbDuration = e.kbTime;
					}

					double mov = easeOut(time, initPos, kbDis, kbDuration, -e.dire) - e.pos;
					mov *= -e.dire;

					kbmove(mov);

					time++;
				} else {
					e.anim.setAnim(UType.IDLE, false);
					if (e.status[P_WARP][0] > 0)
						e.status[P_WARP][0]--;
					if (e.status[P_WARP][1] > 0)
						e.status[P_WARP][1]--;
					EffAnim<WarpEff> ea = effas().A_W;
					if (e.kbTime + 1 == ea.len(WarpEff.EXIT)) {
						kbmove(kbDis);
						kbDis = 0;
						e.anim.getEff(P_WARP);
						e.status[P_WARP][2] = 0;
						e.kbTime -= 11;
					}
				}
				if (kbType == INT_HB && e.data.getRevenge() != null) {
					if (KB_TIME[INT_HB] - e.kbTime == e.data.getRevenge().pre)
						e.basis.getAttack(e.aam.getAttack(e.data.getAtkCount()));
				}
			}
		}
	}

	private static class PoisonToken extends BattleObj {

		private final Entity e;

		private final List<POISON> list = new ArrayList<>();

		private PoisonToken(Entity ent) {
			e = ent;
		}

		private void add(POISON ws) {
			if (ws.type.unstackable)
				list.removeIf(e -> e.type.unstackable && type(e) == type(ws));
			ws.prob = 0; // used as counter
			list.add(ws);
			getMax();
		}

		private void damage(int dmg, int type) {
			type &= 7;
			long mul = type == 0 ? 100 : type == 1 ? e.maxH : type == 2 ? e.health : (e.maxH - e.health);
			e.damage += mul * dmg / 100;

		}

		private void getMax() {
			int max = 0;
			for (int i = 0; i < list.size(); i++)
				max |= 1 << type(list.get(i));
			e.status[P_POISON][0] = max;
		}

		private int type(POISON ws) {
			return ws.type.damage_type + (ws.damage < 0 ? 4 : 0);
		}

		private void update() {
			for (int i = 0; i < list.size(); i++) {
				POISON ws = list.get(i);
				if (ws.time > 0) {
					ws.time--;
					ws.prob--;// used as counter for itv
					if (e.health > 0 && ws.prob <= 0) {
						damage(ws.damage, type(ws));
						ws.prob += ws.itv;
					}
				}
			}
			list.removeIf(w -> w.time <= 0);
			getMax();
		}

	}

	private static class WeakToken extends BattleObj {

		private final Entity e;

		private final List<int[]> list = new ArrayList<>();

		private WeakToken(Entity ent) {
			e = ent;
		}

		private void add(int[] is) {
			list.add(is);
			getMax();
		}

		private void getMax() {
			int max = 0;
			int val = list.isEmpty() ? 100 : list.get(0)[1];
			for (int i = 0; i < list.size(); i++) {
				int[] ws = list.get(i);
				max = Math.max(max, ws[0]);
				val = Math.min(val, ws[1]);
			}
			e.status[P_WEAK][0] = max;

			double ov = e.status[P_WEAK][1];

			e.status[P_WEAK][1] = val;

			if (ov > 100 && val <= 100) {
				if (e.dire == -1) {
					e.anim.effs[A_WEAK_UP] = null;
				} else {
					e.anim.effs[A_E_WEAK_UP] = null;
				}
			}
		}

		private void update() {
			for (int i = 0; i < list.size(); i++)
				list.get(i)[0]--;
			list.removeIf(w -> w[0] <= 0);
			getMax();
		}

	}

	private static class ZombX extends BattleObj {

		private final Entity e;

		private final Set<Entity> list = new HashSet<>();

		/**
		 * temp field: marker for zombie killer
		 */
		private boolean tempZK;

		private int extraRev = 0;

		private ZombX(Entity ent) {
			e = ent;
		}

		private int canRevive() {
			if (e.status[P_REVIVE][0] != 0)
				return 1;
			int tot = totExRev();
			if (tot == -1 || tot > extraRev)
				return 2;
			return 0;
		}

		private boolean canZK() {
			if (e.getProc().REVIVE.type.imu_zkill)
				return false;
			for (Entity zx : list)
				if (zx.getProc().REVIVE.type.imu_zkill)
					return false;
			return true;
		}

		private void damaged(AttackAb atk) {
			tempZK |= (atk.abi & AB_ZKILL) > 0 && canZK();
		}

		private void doRevive(int c) {
			int deadAnim = minRevTime();
			EffAnim<ZombieEff> ea = effas().A_ZOMBIE;
			deadAnim += ea.getEAnim(ZombieEff.REVIVE).len();
			e.status[P_REVIVE][1] = deadAnim;
			e.health = e.maxH * maxRevHealth() / 100;
			if (c == 1)
				e.status[P_REVIVE][0]--;
			else if (c == 2)
				extraRev++;
		}

		private int maxRevHealth() {
			int max = e.getProc().REVIVE.health;
			if (e.status[P_REVIVE][0] == 0)
				max = 0;
			for (Entity zx : list) {
				int val = zx.getProc().REVIVE.health;
				max = Math.max(max, val);
			}
			return max;
		}

		private int minRevTime() {
			int min = e.getProc().REVIVE.time;
			if (e.status[P_REVIVE][0] == 0)
				min = Integer.MAX_VALUE;
			for (Entity zx : list) {
				int val = zx.getProc().REVIVE.time;
				min = Math.min(min, val);
			}
			return min;
		}

		private void postUpdate() {
			if (e.health > 0)
				tempZK = false;
		}

		private boolean prekill() {
			int c = canRevive();
			if (!tempZK && c > 0) {
				int[][] status = e.status;
				doRevive(c);
				// clear state
				e.waitTime = 0;
				e.bdist = 0;
				status[P_BURROW][2] = 0;
				status[P_STOP] = new int[PROC_WIDTH];
				status[P_SLOW] = new int[PROC_WIDTH];
				status[P_WEAK] = new int[PROC_WIDTH];
				status[P_CURSE] = new int[PROC_WIDTH];
				status[P_SEAL] = new int[PROC_WIDTH];
				status[P_STRONG] = new int[PROC_WIDTH];
				status[P_LETHAL] = new int[PROC_WIDTH];
				status[P_POISON] = new int[PROC_WIDTH];
				return true;
			}
			return false;
		}

		private int totExRev() {
			int sum = 0;
			for (Entity zx : list) {
				int val = zx.getProc().REVIVE.count;
				if (val == -1)
					return -1;
				sum += val;
			}
			return sum;
		}

		/**
		 * update revive status
		 */
		private void updateRevive() {
			int[][] status = e.status;
			AnimManager anim = e.anim;

			list.removeIf(em -> {
				int conf = em.getProc().REVIVE.type.range_type;
				if (conf == 3)
					return false;
				if (conf == 2 || em.kbTime == -1)
					return em.kbTime == -1;
				return true;
			});
			List<AbEntity> lm = e.basis.inRange(TCH_ZOMBX, -e.dire, 0, e.basis.st.len, false);
			for (int i = 0; i < lm.size(); i++) {
				if (lm.get(i) == e)
					continue;
				Entity em = ((Entity) lm.get(i));
				double d0 = em.pos + em.getProc().REVIVE.dis_0;
				double d1 = em.pos + em.getProc().REVIVE.dis_1;
				if ((d0 - e.pos) * (d1 - e.pos) > 0)
					continue;
				if (em.kb.kbType == INT_WARP)
					continue;
				REVIVE.TYPE conf = em.getProc().REVIVE.type;
				if (!conf.revive_non_zombie && (e.type & TB_ZOMBIE) == 0)
					continue;
				int type = conf.range_type;
				if (type == 0 && (em.touchable() & (TCH_N | TCH_EX)) == 0)
					continue;
				list.add(em);
			}

			if (status[P_REVIVE][1] > 0) {
				e.acted = true;
				EffAnim<ZombieEff> ea = e.dire == -1 ? effas().A_U_ZOMBIE : effas().A_ZOMBIE;
				if (anim.corpse == null) {
					anim.corpse = ea.getEAnim(ZombieEff.DOWN);
					anim.corpse.setTime(0);
				}
				if (status[P_REVIVE][1] == ea.getEAnim(ZombieEff.REVIVE).len()) {
					anim.corpse = ea.getEAnim(ZombieEff.REVIVE);
					anim.corpse.setTime(0);
				}
				status[P_REVIVE][1]--;
				if (anim.corpse != null)
					anim.corpse.update(false);
				if (status[P_REVIVE][1] == 0)
					anim.corpse = null;
			}
		}

	}

	public final AnimManager anim;

	private final AtkManager atkm;

	private final ZombX zx = new ZombX(this);

	/**
	 * game engine, contains environment configuration
	 */
	public final StageBasis basis;

	/**
	 * entity data, read only
	 */
	public final MaskEntity data;

	/**
	 * group, used for search
	 */
	public int group;

	private final KBManager kb = new KBManager(this);

	/**
	 * layer of display, constant field
	 */
	public int layer;

	/**
	 * proc status, contains ability-specific status data
	 */
	public final int[][] status = new int[PROC_TOT][PROC_WIDTH];

	/**
	 * trait of enemy, also target trait of unit, use bitmask
	 */
	public int type;

	/**
	 * attack model
	 */
	protected final AtkModelEntity aam;

	/**
	 * temp field: damage accumulation
	 */
	private long damage;

	/**
	 * const field
	 */
	protected boolean isBase;

	/**
	 * KB FSM time, values: <br>
	 * 0: not KB <br>
	 * -1: dead <br>
	 * positive: KB time count-down <br>
	 * negative: burrow FSM type
	 */
	private int kbTime;

	/**
	 * temp field: marker for double income
	 */
	protected boolean tempearn;

	/**
	 * wait FSM time
	 */
	private int waitTime;

	/**
	 * acted: temp field, for update sync
	 */
	private boolean acted;

	/**
	 * barrier value, 0 means no barrier or broken
	 */
	private int barrier;

	/**
	 * remaining burrow distance
	 */
	private double bdist;

	/**
	 * poison proc processor
	 */
	private final PoisonToken pois = new PoisonToken(this);

	/**
	 * abilities that are activated after it's attacked
	 */
	private final List<AttackAb> tokens = new ArrayList<>();

	/**
	 * temp field within an update loop <br>
	 * used for moving determination
	 */
	private boolean touch;

	/**
	 * temp field: whether it can attack
	 */
	private boolean touchEnemy;

	/**
	 * weak proc processor
	 */
	private final WeakToken weaks = new WeakToken(this);

	private int altAbi = 0;

	private final Proc sealed = Proc.blank();

	/**
	 * determines when the entity can burrow
	 */
	protected boolean canBurrow = true;

	/**
	 * temporary value for move check
	 */
	protected boolean moved = false;

	/**
	 * Entity's shield hp
	 */
	private int currentShield;

	/**
	 * Used for regenerating shield considering enemy's magnification
	 */
	double shieldMagnification = 1.0;

	protected Entity(StageBasis b, MaskEntity de, EAnimU ea, double atkMagnif, double hpMagnif) {
		super((int) (de.getHp() * hpMagnif));
		basis = b;
		data = de;
		aam = AtkModelEntity.getEnemyAtk(this, atkMagnif);
		anim = new AnimManager(this, ea);
		atkm = new AtkManager(this);
		barrier = de.getShield();
		status[P_BURROW][0] = getProc().BURROW.count;
		status[P_REVIVE][0] = getProc().REVIVE.count;
		sealed.BURROW.set(data.getProc().BURROW);
		sealed.REVIVE.count = data.getProc().REVIVE.count;
		sealed.REVIVE.time = data.getProc().REVIVE.time;
		sealed.REVIVE.health = data.getProc().REVIVE.health;
		currentShield = (int) (de.getProc().DEMONSHIELD.hp * hpMagnif);
		shieldMagnification = hpMagnif;
	}

	protected Entity(StageBasis b, MaskEntity de, EAnimU ea, double lvMagnif, double tAtk, double tHP, PCoin pc, Level lv) {
		super((pc != null && lv != null) ?
				(int) ((1 + b.b.getInc(Data.C_DEF) * 0.01) * (int) ((int) (Math.round(de.getHp() * lvMagnif) * tHP) * pc.getHPMultiplication(lv.getLvs()))) :
				(int) ((1 + b.b.getInc(Data.C_DEF) * 0.01) * (int) (Math.round(de.getHp() * lvMagnif) * tHP))
		);
		basis = b;
		data = de;
		aam = AtkModelEntity.getUnitAtk(this, tAtk, lvMagnif, pc, lv);
		anim = new AnimManager(this, ea);
		atkm = new AtkManager(this);
		barrier = de.getShield();
		status[P_BURROW][0] = getProc().BURROW.count;
		status[P_REVIVE][0] = getProc().REVIVE.count;
		sealed.BURROW.set(data.getProc().BURROW);
		sealed.REVIVE.count = data.getProc().REVIVE.count;
		sealed.REVIVE.time = data.getProc().REVIVE.time;
		sealed.REVIVE.health = data.getProc().REVIVE.health;
		currentShield = de.getProc().DEMONSHIELD.hp;
	}

	public void altAbi(int alt) {
		altAbi ^= alt;

	}

	/**
	 * accept attack
	 */
	@Override
	public void damaged(AttackAb atk) {
		int dmg = getDamage(atk, atk.atk);
		// if immune to wave and the attack is wave, jump out
		if ((atk.waveType & WT_WAVE) > 0 || (atk.waveType & WT_MINI) > 0) {
			if (getProc().IMUWAVE.mult > 0)
				anim.getEff(P_WAVE);
			if (getProc().IMUWAVE.mult == 100)
				return;
			else
				dmg = dmg * (100 - getProc().IMUWAVE.mult) / 100;
		}

		if ((atk.waveType & WT_MOVE) > 0)
			if ((getAbi() & AB_MOVEI) > 0) {
				anim.getEff(P_WAVE);
				return;
			}

		if ((atk.waveType & WT_VOLC) > 0) {
			if (getProc().IMUVOLC.mult > 0) {
				anim.getEff(P_WAVE);
				return;
			}
		}

		tokens.add(atk);

		Proc.PT imuatk = data.getProc().IMUATK;
		if ((atk.dire == -1 || atk.type == -1 || receive(atk.type, -1)) && imuatk.prob > 0) {
			if (status[P_IMUATK][0] == 0 && basis.r.nextDouble() * 100 < imuatk.prob) {
				status[P_IMUATK][0] = (int) (imuatk.time * (1 + 0.2 / 3 * getFruit(atk.type, -1)));
				anim.getEff(P_IMUATK);
			}
			if (status[P_IMUATK][0] > 0)
				return;
		}

		boolean barrierContinue = false;
		boolean shieldContinue = false;

		if (barrier > 0) {
			if (atk.getProc().BREAK.prob > 0) {
				barrier = 0;
				anim.getEff(BREAK_ABI);
				barrierContinue = true;
			} else if (getDamage(atk, atk.atk) >= barrier) {
				barrier = 0;
				anim.getEff(BREAK_ATK);
			} else {
				anim.getEff(BREAK_NON);
			}
		} else {
			barrierContinue = true;
		}

		if(currentShield > 0) {
			if(atk.getProc().SHIELDBREAK.prob > 0) {
				currentShield = 0;

				anim.getEff(SHIELD_BREAKER);

				shieldContinue = true;
			} else if(getDamage(atk, atk.atk) >= currentShield) {
				currentShield = 0;

				anim.getEff(SHIELD_BROKEN);

				cancelAllProc();
			} else {
				currentShield -= getDamage(atk, atk.atk);

				anim.getEff(SHIELD_HIT);

				cancelAllProc();
			}
		} else {
			shieldContinue = true;
		}

		if(!barrierContinue || !shieldContinue) {
			return;
		}

		CommonStatic.setSE(isBase ? SE_HIT_BASE : (basis.r.irDouble() < 0.5 ? SE_HIT_0 : SE_HIT_1));

		damage += dmg;
		zx.damaged(atk);
		tempearn |= (atk.abi & AB_EARN) > 0;

		if(atk instanceof AttackSimple && atk.atk < 0)
			anim.getEff(HEAL);

		//75.0 is guessed value compared from BC
		if(atk.isLongAtk || atk instanceof AttackVolcano)
			anim.smoke = effas().A_WHITE_SMOKE.getEAnim(DefEff.DEF);
		else
			anim.smoke = effas().A_ATK_SMOKE.getEAnim(DefEff.DEF);

		anim.smokeLayer = (int) (layer + 3 - basis.r.nextDouble() * -6);
		anim.smokeX = (int) (pos + 25 - basis.r.nextDouble() * -50);

		//75.0 is guessed value compared from BC
		if (atk.getProc().CRIT.mult > 0) {
			basis.lea.add(new EAnimCont(pos, layer, effas().A_CRIT.getEAnim(DefEff.DEF), -75.0));
			basis.lea.sort(Comparator.comparingInt(e -> e.layer));
			CommonStatic.setSE(SE_CRIT);
		}

		//75.0 is guessed value compared from BC
		if (atk.getProc().SATK.mult > 0) {
			basis.lea.add(new EAnimCont(pos, layer, effas().A_SATK.getEAnim(DefEff.DEF), -75.0));
			basis.lea.sort(Comparator.comparingInt(e -> e.layer));
			CommonStatic.setSE(SE_SATK);
		}

		// process proc part
		if (atk.type != -1 && !receive(atk.type, 1))
			return;

		if (atk.getProc().POIATK.mult > 0) {
			int rst = getProc().IMUPOIATK.mult;
			if (rst == 100) {
				anim.getEff(INV);
			} else {
				damage += maxH * atk.getProc().POIATK.mult / 100;
				basis.lea.add(new EAnimCont(pos, layer, effas().A_POISON.getEAnim(DefEff.DEF)));
				basis.lea.sort(Comparator.comparingInt(e -> e.layer));
				CommonStatic.setSE(SE_POISON);
			}
		}

		if (!isBase && atk.getProc().ARMOR.time > 0) {
			status[P_ARMOR][0] = atk.getProc().ARMOR.time;
			status[P_ARMOR][1] = atk.getProc().ARMOR.mult;
			anim.getEff(P_ARMOR);
		}

		double f = getFruit(atk.type, 1);
		double time = atk instanceof AttackCanon ? 1 : 1 + f * 0.2 / 3;
		double dist = 1 + f * 0.1;
		if (atk.type < 0 || atk.canon != -2)
			dist = time = 1;
		if (atk.getProc().STOP.time > 0) {
			int val = (int) (atk.getProc().STOP.time * time);
			int rst = getProc().IMUSTOP.mult;
			val = val * (100 - rst) / 100;
			status[P_STOP][0] = Math.max(status[P_STOP][0], val);
			if (rst < 100)
				anim.getEff(P_STOP);
			else
				anim.getEff(INV);
		}
		if (atk.getProc().SLOW.time > 0) {
			int val = (int) (atk.getProc().SLOW.time * time);
			int rst = getProc().IMUSLOW.mult;
			val = val * (100 - rst) / 100;
			status[P_SLOW][0] = Math.max(status[P_SLOW][0], val);
			if (rst < 100)
				anim.getEff(P_SLOW);
			else
				anim.getEff(INV);
		}
		if (atk.getProc().WEAK.time > 0) {
			int val = (int) (atk.getProc().WEAK.time * time);
			int rst = getProc().IMUWEAK.mult;
			val = val * (100 - rst) / 100;
			if (rst < 100) {
				weaks.add(new int[] { val, atk.getProc().WEAK.mult });
				anim.getEff(P_WEAK);
			} else
				anim.getEff(INV);
		}
		if (atk.getProc().CURSE.time > 0) {
			int val = (int) (atk.getProc().CURSE.time * time);
			int rst = getProc().IMUCURSE.mult;
			val = val * (100 - rst) / 100;
			status[P_CURSE][0] = Math.max(status[P_CURSE][0], val);
			if (rst < 100)
				anim.getEff(P_CURSE);
			else
				anim.getEff(INV);
		}
		if (atk.getProc().KB.dis != 0) {
			int rst = getProc().IMUKB.mult;
			if (rst < 100) {
				status[P_KB][0] = atk.getProc().KB.time;
				interrupt(P_KB, atk.getProc().KB.dis * dist * (100 - rst) / 100);
			} else
				anim.getEff(INV);
		}
		if (atk.getProc().SNIPER.prob > 0)
			interrupt(INT_ASS, KB_DIS[INT_ASS]);

		if (atk.getProc().BOSS.prob > 0)
			interrupt(INT_SW, KB_DIS[INT_SW]);

		if (atk.getProc().WARP.exists())
			if (getProc().IMUWARP.mult < 100) {
				interrupt(INT_WARP, atk.getProc().WARP.dis);
				EffAnim<WarpEff> e = effas().A_W;
				int len = e.len(WarpEff.ENTER) + e.len(WarpEff.EXIT);
				int val = atk.getProc().WARP.time;
				int rst = getProc().IMUWARP.mult;
				val = val * (100 - rst) / 100;
				status[P_WARP][0] = val + len;
			} else
				anim.getEff(INVWARP);

		if (atk.getProc().SEAL.time > 0)
			if ((getAbi() & AB_SEALI) == 0) {
				status[P_SEAL][0] = atk.getProc().SEAL.time;
				anim.getEff(P_SEAL);
			} else
				anim.getEff(INV);

		if (atk.getProc().POISON.time > 0)
			if ((getAbi() & AB_POII) == 0 || atk.getProc().POISON.damage < 0) {
				POISON ws = (POISON) atk.getProc().POISON.clone();
				pois.add(ws);
				anim.getEff(P_POISON);
			} else
				anim.getEff(INV);

		if (atk.getProc().SPEED.time > 0) {
			status[P_SPEED][0] = atk.getProc().SPEED.time;
			status[P_SPEED][1] = atk.getProc().SPEED.speed;
			status[P_SPEED][2] = atk.getProc().SPEED.type;

			anim.getEff(P_SPEED);
		}
	}

	/**
	 * get the current ability bitmask
	 */
	@Override
	public int getAbi() {
		if (status[P_SEAL][0] > 0)
			return (data.getAbi() ^ altAbi) & (AB_ONLY | AB_METALIC | AB_GLASS);
		return data.getAbi() ^ altAbi;
	}

	/**
	 * get the currently attack, only used in display
	 */
	public int getAtk() {
		return aam.getAtk();
	}

	/**
	 * get the current proc array
	 */
	public Proc getProc() {
		if (status[P_SEAL][0] > 0)
			return sealed;
		return data.getProc();
	}

	/**
	 * receive an interrupt
	 */
	public void interrupt(int t, double d) {
		if(isBase && health <= 0)
			return;

		kb.interrupt(t, d);
	}

	@Override
	public boolean isBase() {
		return isBase;
	}

	/**
	 * mark it dead, proceed death animation
	 *
	 * if atk is true, it means it dies because of suicide ability
	 */
	public void kill(boolean atk) {
		if (kbTime == -1)
			return;
		kbTime = -1;
		atkm.stopAtk();
		anim.kill();
	}

	/**
	 * update the entity after receiving attacks
	 */
	@Override
	public void postUpdate() {
		int hb = data.getHb();
		long ext = health * hb % maxH;
		if (ext == 0)
			ext = maxH;
		if (status[P_ARMOR][0] > 0) {
			damage *= (100 + status[P_ARMOR][1]) / 100.0;
		}
		if (!isBase && damage > 0 && kbTime <= 0 && kbTime != -1 && (ext <= damage * hb || health < damage))
			interrupt(INT_HB, KB_DIS[INT_HB]);
		health -= damage;

		if (health > maxH)
			health = maxH;
		damage = 0;

		// increase damage
		int strong = getProc().STRONG.health;
		if ((touchable() & TCH_CORPSE) == 0 && status[P_STRONG][0] == 0 && strong > 0 && health * 100 <= maxH * strong) {
			status[P_STRONG][0] = getProc().STRONG.mult;
			anim.getEff(P_STRONG);
		}
		// lethal strike
		if (getProc().LETHAL.prob > 0 && health <= 0) {
			boolean b = basis.r.nextDouble() * 100 < getProc().LETHAL.prob;
			if (status[P_LETHAL][0] == 0 && b) {
				health = 1;
				anim.getEff(P_LETHAL);
			}
			status[P_LETHAL][0]++;
		}

		for (int i = 0; i < tokens.size(); i++)
			tokens.get(i).model.invokeLater(tokens.get(i), this);
		tokens.clear();

		if(isBase && health <= 0)
			kbTime = 1;

		kb.doInterrupt();

		if ((getAbi() & AB_GLASS) > 0 && atkm.atkTime == 0 && kbTime == 0 && atkm.loop == 0)
			kill(true);

		// update animations
		anim.update();
		zx.postUpdate();

		if (health > 0)
			tempearn = false;

		if (isBase && health < 0) {
			health = 0;
			atkm.stopAtk();
			anim.setAnim(UType.HB, true);
		}

		acted = false;
	}

	public void setSummon(int conf) {
		// conf 1
		if (conf == 1) {
			kb.kbType = INT_WARP;
			kbTime = effas().A_W.len(WarpEff.EXIT);
			status[P_WARP][2] = 1;
		}
		// conf 2
		if (conf == 2 && data.getPack().anim.anims.length >= 7) {
			kbTime = -3;
			bdist = -1;
		}

		if (conf == 3 && data.getPack().anim.anims.length >= 7) {
			kbTime = -3;
			status[P_BURROW] = new int[PROC_WIDTH];
			bdist = -1;
		}
	}

	/**
	 * can be targeted by the cat with Target ability of trait t
	 */
	@Override
	public boolean targetable(int t) {
		return (type & t) > 0 || isBase;
	}

	/**
	 * get touch mode bitmask
	 */
	@Override
	public int touchable() {
		int n = (getAbi() & AB_GHOST) > 0 ? TCH_EX : TCH_N;
		int ex = getProc().REVIVE.type.revive_others ? TCH_ZOMBX : 0;
		if (kbTime == -1)
			return TCH_SOUL | ex;
		if (status[P_REVIVE][1] >= REVIVE_SHOW_TIME)
			return TCH_CORPSE | ex;
		if (status[P_BURROW][2] > 0)
			return n | TCH_UG | ex;
		if (kbTime < -1)
			return TCH_UG | ex;
		if (anim.anim.type == UType.ENTER)
			return TCH_ENTER | ex;
		return (kbTime == 0 ? n : TCH_KB) | ex;
	}

	/**
	 * update the entity. order of update: <br>
	 *  move -> KB -> revive -> burrow -> wait -> attack
	 */
	@Override
	public void update() {
		// update proc effects
		updateProc();

		boolean nstop = status[P_STOP][0] == 0;
		canBurrow |= atkm.loop < data.getAtkLoop() - 1;

		// do move check if available, move if possible
		if (kbTime == 0 && !acted && atkm.atkTime == 0 && status[P_REVIVE][1] == 0 && anim.anim.type != UType.ENTER) {
			checkTouch();

			if (!touch && nstop) {
				if (health > 0)
					anim.setAnim(UType.WALK, true);
				updateMove(-1, 0);
			}
		}

		// update revive status, mark acted
		zx.updateRevive();

		// check touch after KB or move
		checkTouch();

		// update burrow state if not stopped
		if (nstop && canBurrow)
			updateBurrow();

		// update wait and attack state
		if (kbTime == 0 && anim.anim.type != UType.ENTER) {
			boolean binatk = waitTime + atkm.atkTime == 0;
			binatk &= touchEnemy && atkm.loop != 0 && nstop;

			// if it can attack, setup attack state
			if (!acted && binatk && !(isBase && health <= 0))
				atkm.setUp();

			// update waiting state
			if ((waitTime >= 0 || !touchEnemy) && touch && atkm.atkTime == 0 && !(isBase && health <= 0))
				anim.setAnim(UType.IDLE, true);
		}
		if (waitTime > 0)
			waitTime--;

		// update attack status when in attack state
		if (atkm.atkTime > 0 && nstop)
			atkm.updateAttack();
	}

	protected int critCalc(boolean isMetal, int ans, AttackAb atk) {
		int satk = atk.getProc().SATK.mult;
		if (satk > 0)
			ans *= (100 + satk) * 0.01;
		int crit = atk.getProc().CRIT.mult;
		if (getProc().CRITI.type == 1)
			crit = 0;
		if (isMetal)
			if (crit > 0)
				ans *= 0.01 * crit;
			else if (crit < 0)
				ans = (int) Math.ceil(health * crit / -100.0);
			else
				ans = ans > 0 ? 1 : 0;
		else if (crit > 0)
			ans *= 0.01 * crit;
		else if (crit < 0)
			ans = (int) Math.ceil(maxH * 0.0001);
		return ans;
	}

	/**
	 * determine the amount of damage received from this attack
	 */
	protected abstract int getDamage(AttackAb atk, int ans);

	/**
	 * get max distance to go back
	 */
	protected abstract double getLim();

	protected abstract int traitType();

	/**
	 * move forward <br>
	 * maxl: max distance to move <br>
	 * extmov: distance try to add to this movement return false when movement reach
	 * endpoint
	 */
	protected boolean updateMove(double maxl, double extmov) {
		if (moved)
			canBurrow = true;
		moved = true;

		double mov = status[P_SLOW][0] > 0 ? 0.5 : data.getSpeed() * 0.5;

		if (status[P_SPEED][0] > 0 && status[P_SLOW][0] <= 0) {
			if (status[P_SPEED][2] == 0) {
				mov += status[P_SPEED][1] * 0.5;
			} else if (status[P_SPEED][2] == 1) {
				mov = mov * (100 + status[P_SPEED][1]) / 100;
			} else if (status[P_SPEED][2] == 2) {
				mov = status[P_SPEED][1] * 0.5;
			}
		}

		if (cantGoMore()) {
			mov = 0;
		}

		mov += extmov;

		if(maxl > 0)
			mov = Math.min(mov, maxl);

		pos += mov * dire;

		return maxl > mov;
	}

	private boolean cantGoMore() {
		if (status[P_SPEED][0] == 0)
			return false;

		if (dire == 1) {
			return pos <= 0;
		} else {
			return pos >= basis.st.len;
		}
	}

	/**
	 * interrupt whatever this entity is doing
	 */
	private void clearState() {
		atkm.stopAtk();
		if (kbTime < -1 || status[P_BURROW][2] > 0) {
			status[P_BURROW][2] = 0;
			bdist = 0;
			kbTime = 0;
		}
		if (status[P_REVIVE][1] > 0) {
			status[P_REVIVE][1] = 0;
			anim.corpse = null;
		}
	}

	private void drawAxis(FakeGraphics gra, P p, double siz) {
		// after this is the drawing of hit boxes
		siz *= 1.25;
		double rat = BattleConst.ratio;
		double poa = p.x - pos * rat * siz;
		int py = (int) p.y;
		int h = (int) (640 * rat * siz);
		gra.setColor(FakeGraphics.RED);
		for (int i = 0; i < data.getAtkCount(); i++) {
			double[] ds = aam.inRange(i);
			double d0 = Math.min(ds[0], ds[1]);
			double ra = Math.abs(ds[0] - ds[1]);
			int x = (int) (d0 * rat * siz + poa);
			int y = (int) (p.y + 100 * i * rat * siz);
			int w = (int) (ra * rat * siz);
			if (atkm.tempAtk == i)
				gra.fillRect(x, y, w, h);
			else
				gra.drawRect(x, y, w, h);
		}
		gra.setColor(FakeGraphics.YELLOW);
		int x = (int) ((pos + data.getRange() * dire) * rat * siz + poa);
		gra.drawLine(x, py, x, py + h);
		gra.setColor(FakeGraphics.BLUE);
		int bx = (int) ((dire == -1 ? pos : pos - data.getWidth()) * rat * siz + poa);
		int bw = (int) (data.getWidth() * rat * siz);
		gra.drawRect(bx, (int) p.y, bw, h);
		gra.setColor(FakeGraphics.CYAN);
		gra.drawLine((int) (pos * rat * siz + poa), py, (int) (pos * rat * siz + poa), py + h);
		atkm.tempAtk = -1;
	}

	/**
	 * get the extra proc time due to fruits, for EEnemy only
	 */
	private double getFruit(int atktype, int dire) {
		if (traitType() != dire)
			return 0;
		return basis.b.t().getFruit(atktype & type);
	}

	/**
	 * called when last KB reached
	 */
	private void preKill() {
		CommonStatic.setSE(basis.r.irDouble() < 0.5 ? SE_DEATH_0 : SE_DEATH_1);

		if (zx.prekill())
			return;
		kill(false);
	}

	/**
	 * can be effected by the ability targeting trait t
	 */
	private boolean receive(int t, int dire) {
		if (traitType() != dire)
			return true;
		return (type & t) > 0;
	}

	/**
	 * update burrow state
	 */
	private void updateBurrow() {
		if (!acted && kbTime == 0 && touch && status[P_BURROW][0] != 0) {
			double bpos = basis.getBase(dire).pos;
			boolean ntbs = (bpos - pos) * dire > data.touchBase();
			if (ntbs) {
				// setup burrow state
				status[P_BURROW][0]--;
				status[P_BURROW][2] = anim.setAnim(UType.BURROW_DOWN, true);
				kbTime = -2;
			}
		}
		if (!acted && kbTime == -2) {
			acted = true;
			// burrow down
			status[P_BURROW][2]--;
			if (status[P_BURROW][2] == 0) {
				kbTime = -3;
				anim.setAnim(UType.BURROW_MOVE, true);
				bdist = data.getRepAtk().getProc().BURROW.dis;
			}
		}
		if (!acted && kbTime == -3) {
			// move underground
			double oripos = pos;
			updateMove(0, 0);
			bdist -= (pos - oripos) * dire;
			if (bdist < 0 || (basis.getBase(dire).pos - pos) * dire - data.touchBase() <= 0) {
				bdist = 0;
				kbTime = -4;
				status[P_BURROW][2] = anim.setAnim(UType.BURROW_UP, true) - 2;
			}
		}
		if (!acted && kbTime == -4) {
			// burrow up
			acted = true;
			status[P_BURROW][2]--;
			if (status[P_BURROW][2] <= 0)
				kbTime = 0;
		}

	}

	/**
	 * update proc status
	 */
	private void updateProc() {
		if (status[P_STOP][0] > 0)
			status[P_STOP][0]--;
		if (status[P_SLOW][0] > 0)
			status[P_SLOW][0]--;
		if (status[P_CURSE][0] > 0)
			status[P_CURSE][0]--;
		if (status[P_SEAL][0] > 0)
			status[P_SEAL][0]--;
		if (status[P_IMUATK][0] > 0)
			status[P_IMUATK][0]--;
		if (status[P_ARMOR][0] > 0)
			status[P_ARMOR][0]--;
		if (status[P_SPEED][0] > 0)
			status[P_SPEED][0]--;
		// update tokens
		weaks.update();
		pois.update();
	}

	/**
	 * verify touch state
	 */
	public void checkTouch() {
		touch = true;
		double[] ds = aam.touchRange();
		List<AbEntity> le = basis.inRange(data.getTouch(), dire, ds[0], ds[1], false);
		boolean blds;
		if (data.isLD() || data.isOmni()) {
			double bpos = basis.getBase(dire).pos;
			blds = (bpos - pos) * dire > data.touchBase();
			if (blds)
				le.remove(basis.getBase(dire));
			if (dire == -1 && pos <= bpos && !le.contains(basis.getBase(dire)))
				le.add(basis.getBase(dire));
			else if(dire == 1 && pos >= bpos && !le.contains(basis.getBase(dire)))
				le.add(basis.getBase(dire));
			blds &= le.size() == 0;
		} else {
			blds = le.size() == 0;
		}
		if (blds)
			touch = false;
		touchEnemy = touch;
		if ((getAbi() & AB_ONLY) > 0) {
			touchEnemy = false;
			for (int i = 0; i < le.size(); i++)
				if (le.get(i).targetable(type))
					touchEnemy = true;
		}
	}

	@Override
	public void preUpdate() {
		// if this entity is in kb state, do kbmove()
		if (kbTime > 0)
			kb.updateKB();
	}

	/**
	 * Remove existing proc to this entity
	 */
	public void cancelAllProc() {
		weaks.list.clear();
		pois.list.clear();

		for(int i = 0; i < REMOVABLE_PROC.length; i++) {
			status[REMOVABLE_PROC[i]][0] = 1;
		}
	}
}
