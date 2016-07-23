/**
 * @author acely
 * 本程序由ACELY创作，开源代码仅供参考交流。不提供任何技术支持和质量保证。
 * 禁止修改、传播、再次分发以及任何商业用途。不得将部分或全部代码应用于其他任何软件。
 */
package modles;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Vector;

import controls.DispTime;
import controls.TimelineX;
import views.HRPane;
import views.TimelinePane;

public class Timeline {
	private Timeline(){}
	private static Timeline timeline = new Timeline();
	public static Timeline getTimeline() {
		return timeline;
	}
	//------------------
	private Vector<HRPane> blocks = new Vector<HRPane>();
	public boolean allow_snap = true;
	public int snap_factor = 10;//吸附距离
	public int track_max = 5;//最大轨道数
	public int track_hight = 25;//轨道高度
	public int time_unit = 100;//每个时间单位的长度（毫秒）
	private int time_unit_width = 3;//每个时间单位在标尺上显示的宽度（像素）
	public int time_total = 12;//时间轴总长度（秒）
	public int default_width = 2000;//块默认长度 ms
	public int ruler_hight = 25;//标尺高度
	private long curr_time = 0;
	public long repeat_start = -1;//循环播放的起始时间
	public long repeat_end = -1;//循环播放的结束时间
	//------------------
	private boolean playing = false;
	//------------------
	private String alltime[][];
	private LinkedList<Long> time_nodes;
	public StringBuilder srtBuilder;
	public  String srtin;
	//------------------
	
	public Vector<HRPane> getBlocks() {
		return blocks;
	}
	
	public void add_block(HRPane newblock,int layer,boolean visiable) {
		newblock.layer = layer;
		blocks.add(newblock);
		//System.out.println("added:"+newblock.getName());
		if (visiable) {
			TimelinePane.getTLPane().add_block_by_user(newblock);
		}else {
			TimelinePane.getTLPane().add_block(newblock);
		}
	}
	
	public void add_block_below(String name) {
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).getName().equals(name)) {
				HRPane ori = blocks.get(i);
				if (ori.layer < track_max) {
					HRPane newHrPane = new HRPane("->");
					newHrPane.time_start = ori.time_start;
					newHrPane.time_end = ori.time_end;
					newHrPane.setLocation(ori.getX(),1+ruler_hight+ori.layer*track_hight);
					newHrPane.setSize(ori.getSize());
					newHrPane.layer = ori.layer+1;
					blocks.add(newHrPane);
					TimelinePane.getTLPane().add_block(newHrPane);
					TimelinePane.getTLPane().repaint();
					newHrPane.requestFocusInWindow();
				}
				break;
			}
		}
	}
	
	public void remove_block(String hash) {
		for (int i = 0; i < blocks.size(); i++) {
			if (blocks.get(i).getName().equals(hash)) {
				TimelinePane.getTLPane().remove_block(blocks.get(i));
				blocks.removeElementAt(i);
				System.out.println("Block with hash-"+hash+"-removed!");
				break;
			}
		}
		//删除后也刷新文件
		Timeline.getTimeline().EncodeSrt();
		TimelineX.getTimelineX().saveSrtFile();
		TimelineX.tlx.getPlayPane().getMediaPlayer().setSubTitleFile(
		TimelineX.getTimelineX().videofile.substring(0,TimelineX.getTimelineX().videofile.lastIndexOf("."))+".srt");
	}
	
	public void clearAll() {
		blocks = new Vector<HRPane>();
		TimelinePane.getTLPane().removeAll();
		TimelinePane.getTLPane().repaint();
	}
	
	public long getCurrTime() {
		return curr_time;
	}
	
	public void setCurrTime(long t) {
		if (t >= 0) {
			curr_time = t;
		}else {
			curr_time = 0;
		}
		if (!playing) {
			TimelineX.tlx.getPlayPane().getMediaPlayer().setTime(curr_time);
		}
	}
	
	public int getTimeUnitWidth() {
		return time_unit_width;
	}
	
	public void setTimeUnitWidth(int w) {
		if (w >= 1) {
			time_unit_width = w;
		}
	}
	
	public boolean getPlaying() {
		return playing;
	}
	
	public void setPlaying(boolean s){
		playing = s;
	}
	
	/**
	 * 将时间轴上得所有块转换为SRT时间轴数据
	 */
	public void EncodeSrt() {
		alltime = new String[blocks.size()][4];
		time_nodes = new LinkedList<Long>();
		srtBuilder = new StringBuilder();
		//构建新alltime数组，用来缩短检索时间（Vector检索速度慢）
		for (int i = 0; i < blocks.size(); i++) {
			alltime[i][0] = blocks.get(i).time_start+"";	//0-存放块开始时间
			alltime[i][1] = blocks.get(i).time_end+"";		//1-存放块结束时间
			alltime[i][2] = blocks.get(i).layer+"";			//2-存放块所在层数
			alltime[i][3] = blocks.get(i).getText();		//3-存放块文字内容
			//把所有块的旗帜时间添加到节点库中去（不会重复）
			addToTimeNodes(Long.parseLong(alltime[i][0]));
			addToTimeNodes(Long.parseLong(alltime[i][1]));
		}
		//【非常重要】对所有的时间节点进行排序
		Collections.sort(time_nodes);
		//对每个时间节点进行分析，找出其+1后穿过的块，然后构建对应内容
		int index = 1;//字幕时间轴编号
		for (int i = 0; i < time_nodes.size()-1; i++) {
			String[] content = new String[track_max+1];//按最大层数+1构建一个数组，用来按层存放节点穿过的内容块对应的内容
			long nt = time_nodes.get(i);//存放当前块的开始时间
			long nxt = time_nodes.get(i+1);//存放当前块的结束时间
			//扫描所有块
			for (int j = 0; j < blocks.size(); j++) {
				if (nt+1 > Long.parseLong(alltime[j][0]) && nt+1 < Long.parseLong(alltime[j][1])) {//节点+1是否穿过块
					content[Integer.parseInt(alltime[j][2])] = alltime[j][3];//如果穿过则把文字按照层数放在数组里
				}
			}
			//扫描完所有的块之后，创建真正的SRT时间轴信息
			StringBuilder contentBuilder = new StringBuilder();
			for (int n = 1; n < content.length; n++) {
				if (content[n] != null) {
					contentBuilder.append(content[n]).append("\n");
				}
			}
			if (contentBuilder.toString().equals("")) {
				//如果这个节点没有文字数据，则调到下一个节点，不输出内容
				continue;
			}
			srtBuilder.append(index+"\n");
			srtBuilder.append(DispTime.formatTime(nt)+" --> "+DispTime.formatTime(nxt)+"\n");
			srtBuilder.append(contentBuilder.toString()+"\n");
			index ++;
		}
	}
	
	/**
	 * 
	 * @param t 以毫秒为单位的时间
	 */
	private void addToTimeNodes(long t) {
		//添加时间结点，并且避免重复
		if (time_nodes.size() == 0) {
			time_nodes.add(t);
		}else {
			for (int i = 0; i < time_nodes.size(); i++) {
				if (time_nodes.get(i) == t) {
					return;
				}
			}
			time_nodes.add(t);
		}
	}
	
	public void DecodeSrt() {
		blocks = new Vector<HRPane>();
		String[] allele = srtin.split("\n\n");
		long start = 0;
		long end = 1;
		for (int i = 0; i < allele.length; i++) {
			if (allele[i].contains(" --> ")) {
				String[] lines = allele[i].split("\n");
				start = decodeTime(lines[1].substring(0,12));
				end = decodeTime(lines[1].substring(17,29));
				for (int j = 2; j < lines.length; j++) {
					HRPane newblock = new HRPane(lines[j]);
					newblock.time_start = start;
					newblock.time_end = end;
					add_block(newblock, j-1,false);
				}
			}
		}
		time_total = (int) (start/1000);
		TimelinePane.getTLPane().refresh_block();
	}
	
	public long decodeTime(String tl) {
		//00:00:00,000 --> 00:00:02,000
		//------------     ------------
		
		return Integer.parseInt(tl.substring(3,5))*60000+
				Integer.parseInt(tl.substring(6,8))*1000+
				Integer.parseInt(tl.substring(9,12));
	}
}
