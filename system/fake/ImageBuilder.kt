package common.system.fake

import common.io.assets.Admin
import common.io.assets.Admin.StaticPermitted
import java.io.IOException

abstract class ImageBuilder {
    /**
     * List of parsed Classes (to `Supplier<InputStream>`):
     *  * `VFile`
     *  * `FileData`
     *  * `byte[]`
     * <hr></hr>
     * List of processed Classes:
     *  * `File`
     *  * `Supplier<InputStream>`
     * <hr></hr>
     * List of transfered Classes:
     *  * `FakeImage` return directly
     *  * `BufferedImage` PC only
     *  * `BitMap` android only
     *
     * @throws IOException
     */
    @Throws(IOException::class)
    abstract fun build(o: Any?): FakeImage?
    @Throws(IOException::class)
    abstract fun write(o: FakeImage, fmt: String?, out: Any?): Boolean

    companion object {
        @StaticPermitted(Admin.StaticPermitted.Type.ENV)
        var builder: ImageBuilder? = null
    }
}
