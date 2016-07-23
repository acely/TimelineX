/**
 * @author acely
 * 本程序由ACELY创作，开源代码仅供参考交流。不提供任何技术支持和质量保证。
 * 禁止修改、传播、再次分发以及任何商业用途。不得将部分或全部代码应用于其他任何软件。
 */
 package controls;


import java.util.Timer;
import java.util.TimerTask;

import views.TimelinePane;
import modles.Timeline;

public class Play extends Timer {
	

	public Play(){
		//如果是新加入的视频（还没有播放过），则从头开始播放
		if (TimelineX.getTimelineX().videoJustOpened) {
			Timeline.getTimeline().setCurrTime(0);
			TimelinePane.getTLPane().setPlayhead(1);
			TimelineX.getTimelineX().videoJustOpened = false;
		}
	}
	
	
	class PlayTask extends TimerTask{

		@Override
		public void run() {
			//如果检测到modle停止，则马上退出播放
			if (!Timeline.getTimeline().getPlaying()){
				TimelineX.tlx.getPlayPane().getMediaPlayer().pause();
				System.out.println("Paused");
				cancel();
			}
			//刷新modle中的时间，刷新界面
			Timeline.getTimeline().setCurrTime(Timeline.getTimeline().getCurrTime() + 50);
			TimelinePane.getTLPane().setPlayhead((int)((Timeline.getTimeline().getCurrTime())*(float)Timeline.getTimeline().getTimeUnitWidth()/Timeline.getTimeline().time_unit)+1);
			TimelinePane.getTLPane().repaint();
			//如果选择了块，则循环播放对应时间段
			if (Timeline.getTimeline().repeat_end != -1) {
				if (Timeline.getTimeline().getCurrTime() >= Timeline.getTimeline().repeat_end) {
					Timeline.getTimeline().setCurrTime(Timeline.getTimeline().repeat_start);
					TimelinePane.getTLPane().setPlayhead((int)((Timeline.getTimeline().repeat_start)*(float)Timeline.getTimeline().getTimeUnitWidth()/Timeline.getTimeline().time_unit)+1);
					TimelineX.tlx.getPlayPane().getMediaPlayer().setTime(Timeline.getTimeline().repeat_start);
				}
			}
			//如果播放头到达时间轴末尾，则停止播放，将播放头返回到头部
			if(TimelinePane.getTLPane().getPlayhead() >= TimelinePane.getTLPane().getWidth()) {
				Timeline.getTimeline().setPlaying(false);
				TimelineX.tlx.getPlayPane().getMediaPlayer().pause();
				TimelinePane.getTLPane().setPlayhead(1);
				Timeline.getTimeline().setCurrTime(0);
				System.out.println("Paused");
				cancel();
			}
		}
		
	}

	@Override
	public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
		// TODO Auto-generated method stub
		super.scheduleAtFixedRate(new PlayTask(), 0, 50);
	}
	
}
