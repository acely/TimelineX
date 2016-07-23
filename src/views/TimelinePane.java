/**
 * @author acely
 * 本程序由ACELY创作，开源代码仅供参考交流。不提供任何技术支持和质量保证。
 * 禁止修改、传播、再次分发以及任何商业用途。不得将部分或全部代码应用于其他任何软件。
 */
 package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import controls.TimelineX;
import modles.Timeline;

public class TimelinePane extends JPanel {
	
	//----------------------
	private int playhead = 1;
	public int selected_layer = 1;
	public boolean shiftdrag = false;
	//----------------------
	private JPanel head;
	//----------------------

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static TimelinePane tlPane = new TimelinePane();
	private TimelinePane(){
		setLayout(null);
		setDoubleBuffered(true);
		init();
	}
	public static TimelinePane getTLPane() {
		return tlPane;
	}
	
	
	public long x_to_time(int x) {
		return (long) ((x-1)*((float)Timeline.getTimeline().time_unit/Timeline.getTimeline().getTimeUnitWidth()));
	}
	
	public int time_to_x(long time) {
		return (int)(time*(float)Timeline.getTimeline().getTimeUnitWidth()/Timeline.getTimeline().time_unit)+1;
	}
	
	/**
	 * 由程序添加块，与用户添加块不同
	 */
	public void add_block(HRPane newblock) {
		this.add(newblock);
	}
	
	public void add_block_by_user(HRPane newblock) {
		this.add(newblock);
		//---如果没指定层，则放到当前激活的层上，起始位置与播放头一致---
		if (newblock.layer == 0) {
			newblock.layer = selected_layer;
		}
		newblock.setSize(time_to_x(Timeline.getTimeline().default_width)-1,Timeline.getTimeline().track_hight-1);
		newblock.setLocation(playhead, 1+Timeline.getTimeline().ruler_hight+(newblock.layer-1)*Timeline.getTimeline().track_hight);
		newblock.time_start = x_to_time(playhead);
		newblock.time_end = newblock.time_start+Timeline.getTimeline().default_width;
		//---播放头跟踪---
		if (!Timeline.getTimeline().getPlaying()) {
			//如果正在播放的话，插入块就不更新播放头位置
			playhead += newblock.getWidth();
			Timeline.getTimeline().setCurrTime(x_to_time(playhead));
		}
		Timeline.getTimeline().time_total = (playhead-1)/Timeline.getTimeline().getTimeUnitWidth()/(1000/Timeline.getTimeline().time_unit)+6;
		//如果播放头位置即将大于时间轴宽度，则增加总时间，并加宽时间轴
		if (playhead >= getWidth()) {
			Timeline.getTimeline().time_total = (playhead-1)/Timeline.getTimeline().getTimeUnitWidth()/10+5;
			setSize((Timeline.getTimeline().time_total*(1000/Timeline.getTimeline().time_unit))*Timeline.getTimeline().getTimeUnitWidth(), getHeight());
			setPreferredSize(new Dimension((Timeline.getTimeline().time_total*(1000/Timeline.getTimeline().time_unit))*Timeline.getTimeline().getTimeUnitWidth(), getHeight()));
		}
		repaint();
	}
	
	public void refresh_block() {
		HRPane ablock;
		for (int i = 0; i < Timeline.getTimeline().getBlocks().size(); i++) {
			ablock = Timeline.getTimeline().getBlocks().get(i);
			int x = time_to_x(ablock.time_start);
			ablock.setSize(time_to_x(ablock.time_end)-x,Timeline.getTimeline().track_hight-1);
			ablock.setLocation(x, 1+Timeline.getTimeline().ruler_hight+(ablock.layer-1)*Timeline.getTimeline().track_hight);
		}
		setPreferredSize(new Dimension((Timeline.getTimeline().time_total*(1000/Timeline.getTimeline().time_unit))*Timeline.getTimeline().getTimeUnitWidth(), getHeight()));
		paintAll(getGraphics());
		paintChildren(getGraphics());
		repaint();
	}
	
	public void remove_block(HRPane bye) {
		remove(bye);
		requestFocusInWindow();
		repaint();
	}
	
	public void setPlayhead(int x) {
		playhead = x;
		//head.setLocation(x, 0);
	}
	
	public int getPlayhead() {
		return playhead;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		System.out.println("paint timeline");
		g.setColor(new Color(30,30,30));
		g.fillRect(0, 0, getWidth(), Timeline.getTimeline().ruler_hight);
		g.setColor(new Color(40,40,40));
		g.fillRect(0, Timeline.getTimeline().ruler_hight, getWidth(), getHeight());
		//画标尺
		g.setColor(Color.WHITE);
		for (int i = 0; i <= (Timeline.getTimeline().time_total*1000)/Timeline.getTimeline().time_unit; i++) {
			if (i%(1000/Timeline.getTimeline().time_unit) == 0) {
				g.drawLine(i*Timeline.getTimeline().getTimeUnitWidth()+1, 1, i*Timeline.getTimeline().getTimeUnitWidth()+1, 5);
				//g.drawString((i/(1000/Timeline.getTimeline().time_unit))+"s", i*Timeline.getTimeline().getTimeUnitWidth()-5, Timeline.getTimeline().ruler_hight-2);
			}
			
		}
		//画层框
		g.setColor(new Color(40,50,60));
		g.drawLine(0, Timeline.getTimeline().ruler_hight, getWidth(), Timeline.getTimeline().ruler_hight);
		g.drawLine(0, Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight, getWidth(), Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight);
		g.drawLine(0, Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*2, getWidth(), Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*2);
		g.drawLine(0, Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*3, getWidth(), Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*3);
		g.drawLine(0, Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*4, getWidth(), Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*4);
		g.drawLine(0, Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*5, getWidth(), Timeline.getTimeline().ruler_hight+Timeline.getTimeline().track_hight*5);
		//画选定层的高亮显示
		g.setColor(new Color(40,43,50));
		g.fillRect(0, (selected_layer-1)*Timeline.getTimeline().track_hight+Timeline.getTimeline().ruler_hight+1, getWidth(), Timeline.getTimeline().track_hight-1);
		//画播放头
		g.setColor(Color.RED);
		g.drawLine(playhead,0,playhead,getHeight());
		g.drawLine(playhead-3, 0, playhead+3, 0);
		g.drawLine(playhead-2, 1, playhead+2, 1);
		g.drawLine(playhead-1, 2, playhead+1, 2);
	}
	
	
	private void init() {
		this.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				requestFocus();
				playhead = e.getX();
				Timeline.getTimeline().setCurrTime((long) ((playhead-1)*((float)Timeline.getTimeline().time_unit/Timeline.getTimeline().getTimeUnitWidth())));
				if (e.getY() > Timeline.getTimeline().ruler_hight) {
					selected_layer = (e.getY()-Timeline.getTimeline().ruler_hight)/Timeline.getTimeline().track_hight + 1;
				}
				
				repaint();
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				System.out.println("--------------------");
				Timeline.getTimeline().setPlaying(false);
				playhead = e.getX();
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (e.getX() >= 1) {
					if (e.getX() >= getWidth()) {
						playhead = getWidth();
					}else {
						playhead = e.getX();
					}
					Timeline.getTimeline().setCurrTime((long) ((playhead-1)*((float)Timeline.getTimeline().time_unit/Timeline.getTimeline().getTimeUnitWidth())));
				}else {
					playhead = 1;
					Timeline.getTimeline().setCurrTime(0);
				}
				repaint();
				
			}
		});
		this.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent arg0) {
				shiftdrag = false;
				
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				//System.out.println(k);
				if (e.isShiftDown()) {//垂直拖放
					shiftdrag = true;
				}
				if (k == 32) {//SPACE
					TimelineX.getTimelineX().play();
				}else if (k == 37) {//方向键左
					if (!Timeline.getTimeline().getPlaying() && playhead > 1) {
						if (e.isShiftDown()) {//如果按下Shift则按10像素移动
							playhead -= 10;
						}else {
							playhead --;
						}
						Timeline.getTimeline().setCurrTime((long) ((playhead-1)*((float)Timeline.getTimeline().time_unit/Timeline.getTimeline().getTimeUnitWidth())));
						repaint();
					}
				}else if (k == 39) {//方向键右
					if (!Timeline.getTimeline().getPlaying() && playhead < getWidth()) {
						if (e.isShiftDown()) {//如果按下Shift则按10像素移动
							playhead += 10;
						}else {
							playhead ++;
						}
						Timeline.getTimeline().setCurrTime((long) ((playhead-1)*((float)Timeline.getTimeline().time_unit/Timeline.getTimeline().getTimeUnitWidth())));
						repaint();
					}
				}
			}
		});
		this.addFocusListener(new FocusListener() {
			
			@Override
			public void focusLost(FocusEvent e) {
				((JScrollPane) getParent().getParent()).setBorder(javax.swing.BorderFactory
						.createLineBorder(new java.awt.Color(20,20,0)));
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				((JScrollPane) getParent().getParent()).setBorder(javax.swing.BorderFactory
						.createLineBorder(new java.awt.Color(255, 102, 0)));
			}
		});
	}
	
}
