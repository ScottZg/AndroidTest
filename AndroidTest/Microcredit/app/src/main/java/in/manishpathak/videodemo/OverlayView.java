package in.manishpathak.videodemo;

public class OverlayView {
	
	static {
        System.loadLibrary("hello-jni");
    }
	public native void YUVtoRBG(int[] rgb, byte[] yuv, int width, int height);
}
