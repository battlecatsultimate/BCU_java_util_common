package common.system.files

class FDByte(private val data: ByteArray) : ByteData {
    override fun getBytes(): ByteArray? {
        return data
    }
}
