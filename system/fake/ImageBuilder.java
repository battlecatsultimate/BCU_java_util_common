package common.system.fake;

import java.io.IOException;

import common.io.assets.Admin.StaticPermitted;

public abstract class ImageBuilder {

	@StaticPermitted(StaticPermitted.Type.ENV)
	public static ImageBuilder builder;

	public abstract FakeImage build(Object o) throws IOException;

	public abstract boolean write(FakeImage o, String fmt, Object out) throws IOException;

}
