/**
 * @author acely
 * 本程序由ACELY创作，开源代码仅供参考交流。不提供任何技术支持和质量保证。
 * 禁止修改、传播、再次分发以及任何商业用途。不得将部分或全部代码应用于其他任何软件。
 */
 package controls;

import javax.swing.JLabel;

import modles.Timeline;

public class DispTime extends Thread {

	JLabel time;
	public DispTime(JLabel t){
		time = t;
	}
	
	@Override
	public void run() {
		try {
			while (true) {
				time.setText(formatTime(Timeline.getTimeline().getCurrTime()));
				sleep(50);
			}
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String formatTime(long t) {
		StringBuilder time = new StringBuilder();
		time.append("00:");
		//获得分钟
		if (t/60000 > 9) {
			time.append(t/60000).append(":");
		}else {
			time.append("0").append(t/60000).append(":");
		}
		//获得秒
		if (t%60000/1000 > 9) {
			time.append(t%60000/1000).append(",");
		}else {
			time.append("0").append(t%60000/1000).append(",");
		}
		//获得毫秒
		if (t%1000 > 99) {
			time.append(t%1000);
		}else if (t%1000 > 9) {
			time.append("0").append(t%1000);
		}else {
			time.append("00").append(t%1000);
		}
		return time.toString();
	}
}
