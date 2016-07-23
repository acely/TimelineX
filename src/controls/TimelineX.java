/**
 * @author acely
 * 本程序由ACELY创作，开源代码仅供参考交流。不提供任何技术支持和质量保证。
 * 禁止修改、传播、再次分发以及任何商业用途。不得将部分或全部代码应用于其他任何软件。
 */
 package controls;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.List;

import modles.Timeline;
import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;
import uk.co.caprica.vlcj.runtime.windows.WindowsRuntimeUtil;
import views.TimelinePane;
import views.XWindow;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

/**
 * added to github
 * @author ACELY
 *
 */
public class TimelineX {
	
	private static TimelineX topcontrol = new TimelineX();
	public static TimelineX getTimelineX() {
		return topcontrol;
	}
	private TimelineX(){
		//----------------------------
		if (RuntimeUtil.isWindows()) {
			System.out.println("isWindows");
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), fixFilename("C:/Program Files/VideoLAN/VLC"));
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), fixFilename("D:/Program Files/VideoLAN/VLC"));
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), WindowsRuntimeUtil.getVlcInstallDir());
		}else if (RuntimeUtil.isMac()) {
			NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), fixFilename("/Applications/VLC.app/Contents/MacOS/lib"));
		}
		Native.loadLibrary(RuntimeUtil.getLibVlcLibraryName(),LibVlc.class);
		tlx = new XWindow();
		tlx.setTitle("TimelineX - ACELY STUDIO 2013");
		tlx.setVisible(true);
		TimelinePane.getTLPane().requestFocus();
		//----------------------------------
		//Timeline.getTimeline().add_block(new HRPane("开始创建时间轴"),1);
		//Timeline.getTimeline().add_block(new HRPane("Enjoy TimelineX !"),2);
		if (RuntimeUtil.isWindows()) {
			tlx.getPlayPane().getMediaPlayer().prepareMedia(fixFilename("E:/VIDEO/Girls Are Art/163613116.mp4"));
			videofile = "E:/VIDEO/Girls Are Art/163613116.mp4";
			videoJustOpened = true;
		}else if (RuntimeUtil.isMac()) {
			tlx.getPlayPane().getMediaPlayer().prepareMedia(fixFilename("/Users/apple/Desktop/test.mp4"));
			videofile = "/Users/apple/Desktop/test.mp4";
			videoJustOpened = true;
		}
		readSrtFile();
		tlx.getPlayPane().getMediaPlayer().setRepeat(true);
		System.out.println(tlx.getPlayPane().getMediaPlayerFactory().version());
		//----------------------------------
		
	}
	
	public String fixFilename(String n) {
		File getname = new File(n);
		return getname.getAbsolutePath();
	}

	//-------------------------
	public static XWindow tlx;
	public boolean videoJustOpened;
	public String videofile;
	//-------------------------
	
	public static void main(String[] args) {

	}
	
	public void play() {
		if (Timeline.getTimeline().getPlaying()) {
			Timeline.getTimeline().setPlaying(false);
			System.out.println(tlx.getPlayPane().getMediaPlayer().getLength());
		}else {
			Timeline.getTimeline().setPlaying(true);
			TimelineX.tlx.getPlayPane().getMediaPlayer().play();
			System.out.println("Playing");
			new Play().scheduleAtFixedRate(null, 0, 0);
		}
	}

	public void saveSrtFile() {
		try {
			writeFile(new File(videofile.substring(0,videofile.lastIndexOf("."))+".srt"),
					Timeline.getTimeline().srtBuilder.toString().replace("\n", "\r\n"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void readSrtFile() {
		try {
			File in = new File(videofile.substring(0,videofile.lastIndexOf("."))+".srt");
			if (in.exists()) {
				Timeline.getTimeline().srtin = readFile(in).replace("\r", "");
				Timeline.getTimeline().DecodeSrt();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeFile(File file,String in) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		OutputStreamWriter osw = new OutputStreamWriter(fos, "GB18030");
		osw.write(in);
		osw.flush();
		osw.close();
		fos.close();
	}
	
	public String readFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GB18030");
		long i = file.length();
        char[] buf = new char[(int)(i)];
		isr.read(buf);
		isr.close();
		fis.close();
		return new String(buf);
	}
	
	public void readDroppedFile(DropTargetDropEvent dtde) {
		try {
	        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
	            dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
	            List<?> list = (List<?>) (dtde.getTransferable()
	                    .getTransferData(DataFlavor.javaFileListFlavor));
	            Iterator<?> iterator = list.iterator();
	            while (iterator.hasNext()) {
	                File f = (File) iterator.next();
	                tlx.getPlayPane().getMediaPlayer().prepareMedia(f.getAbsolutePath());
	    			videofile = f.getAbsolutePath();
	    			videoJustOpened = true;
	    			readSrtFile();
	    			tlx.getPlayPane().getMediaPlayer().setRepeat(true);
	    			TimelinePane.getTLPane().setPlayhead(1);
	    			Timeline.getTimeline().clearAll();
	            }
	            dtde.dropComplete(true);
	        }else {
	            dtde.rejectDrop();
	        }
	    } catch (IOException ioe) {
	    } catch (UnsupportedFlavorException e) {}
	}
	
	public void setVolume(int v) {
		tlx.getPlayPane().getMediaPlayer().setVolume(v);
	}
}
