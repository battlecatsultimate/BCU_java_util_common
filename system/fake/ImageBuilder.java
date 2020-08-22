package common.system.fake;

import common.io.assets.Admin.StaticPermitted;

import java.io.IOException;

public abstract class ImageBuilder {

    @StaticPermitted(StaticPermitted.Type.ENV)
    public static ImageBuilder builder;

    /**
     * List of parsed Classes (to {@code Supplier<InputStream>}):
     * <li>{@code VFile}</li>
     * <li>{@code FileData}</li>
     * <li>{@code byte[]}</li>
     * <hr>
     * List of processed Classes:
     * <li>{@code File}</li>
     * <li>{@code Supplier<InputStream>}</li>
     * <hr>
     * List of transfered Classes:
     * <li>{@code FakeImage} return directly</li>
     * <li>{@code BufferedImage} PC only</li>
     * <li>{@code BitMap} android only</li>
     *
     * @throws IOException
     */
    public abstract FakeImage build(Object o) throws IOException;

    public abstract boolean write(FakeImage o, String fmt, Object out) throws IOException;

}
