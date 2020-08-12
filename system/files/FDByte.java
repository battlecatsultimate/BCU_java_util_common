package common.system.files;

public class FDByte implements ByteData {

	private byte[] data;

	public FDByte(byte[] bs) {
		data = bs;
	}

	@Override
	public byte[] getBytes() {
		return data;
	}

}
