/**
 * @author acely
 * 本程序由ACELY创作，开源代码仅供参考交流。不提供任何技术支持和质量保证。
 * 禁止修改、传播、再次分发以及任何商业用途。不得将部分或全部代码应用于其他任何软件。
 */
 package views;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import controls.TimelineX;
import modles.Timeline;

public class HRPane extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JLabel text = new JLabel();
	JTextField edittext = new JTextField();
	//------------
	int resize_anchor = 6;//调整块的宽度
	boolean on = false;
	boolean onleft = false;
	boolean onright = false;
	boolean pressed = false;
	boolean draging = false;
	boolean chosen = false;//has focus
	boolean usesnapvalue = false;//吸附后，从吸附点获取时间值，不使用坐标来计算时间
	boolean editing = false;//为true时不响应鼠标操作
	int drag_type = 0;//在拖拽时是移动还是缩放，1-左侧缩放  2-右侧缩放 3-移动
	int sx = 0;//鼠标按下时的x坐标
	int sy = 0;//鼠标按下时的y坐标
	int ex = 0;//鼠标松开时的x坐标
	int ey = 0;//鼠标松开时的x坐标
	int temp_width = 0;//鼠标按下时的块宽度
	int temp_x = 0;//鼠标按下时块在容器中de的x坐标
	int temp_y = 0;//鼠标按下时块在容器中de的y坐标
	int temp_blockend = 0;
	long snap_to_value = 0;//吸附点的时间值
	public int layer = 0;
	public int group = 1;
	public long time_start = 0;
	public long time_end   = 0;
	//------------
	//运单号200000540865
	public HRPane(String t) {
		setFocusable(true);
		setDoubleBuffered(true);
		setName(hashCode()+"");
		init();
		text.setForeground(new Color(80,240,240));
		edittext.setVisible(false);;
		edittext.setDragEnabled(false);
		edittext.setBackground(Color.DARK_GRAY);
		edittext.setForeground(Color.WHITE);
		edittext.setSelectionColor(Color.LIGHT_GRAY);
		edittext.setSelectedTextColor(Color.BLUE);
		text.setText(t);
		edittext.setText(t);
		setToolTipText(t);
		this.add(text);
		this.add(edittext);
		//--------------
	}

	public void setText(String t) {
		text.setText(t);
		edittext.setText(t);
	}
	
	public String getText() {
		return text.getText();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		//System.out.println("paint block");
		text.setLocation(resize_anchor+5, getHeight()/2-8);
		text.setSize(getWidth()-2*resize_anchor-10,text.getHeight());
		edittext.setLocation(resize_anchor+1,2);
		edittext.setSize(getWidth()-2*resize_anchor-10,Timeline.getTimeline().track_hight-6);
		// TODO Auto-generated method stub
		super.paintComponent(g);
		//画边框
		g.setColor(new Color(20,20,30));
		g.drawRect(0, 0, getWidth()-1, getHeight()-1);
		//画填充
		if (on) {
			g.setColor(new Color(60,80,90));
		}else {
			g.setColor(Color.DARK_GRAY);
		}
		g.fillRect(resize_anchor+1, 1, getWidth()-2*resize_anchor-2, getHeight()-2);
		//画选中标记
		if (chosen) {
			g.setColor(Color.RED);
			g.fillRect(resize_anchor+2, 1, getWidth()-2*resize_anchor-4, 2);
		}
		//画选拖拽中标记
		if (draging) {
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(resize_anchor+2, getHeight()-3, getWidth()-2*resize_anchor-4, 2);
		}
		//左侧的调整块
		if (onleft && !editing) {
			g.setColor(Color.RED);
		}else {
			g.setColor(getColor(group));
		}
		g.fillRect(1, 1, resize_anchor, getHeight()-2);
		//右侧的调整块
		if (onright && !editing) {
			g.setColor(Color.RED);
		}else {
			g.setColor(getColor(group));
		}
		g.fillRect(getWidth()-(resize_anchor+1), 1, resize_anchor, getHeight()-2);
		//---
	}

	private Color getColor(int g) {
		switch (g) {
		case 1:
			return new Color(255,160,0);
		case 2:
			return new Color(80,225,25);
		case 3:
			return new Color(10,240,250);
		case 4:
			return new Color(20,125,255);
		case 5:
			return new Color(250,75,135);
		default:return Color.ORANGE;
		}
		
	}
	
	private void init() {
		this.addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseMoved(MouseEvent e) {
				if (editing) {
					return;
				}
				int x = e.getX();
				int y = e.getY();
				//左侧的调整块感应
				if (x >= 1 && x <= resize_anchor+1 && y >= 1 && y<= getHeight()-2) {
					boolean onleft_ori = onleft;
					onleft = true;
					if (onleft_ori != onleft) {//大大减少了repaint()的调用次数
						setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
						repaint();
					}
				}else {
					//右侧的调整块感应
					if (x >= getWidth()-(resize_anchor+1) && x <= getWidth()-2 && y >= 1 && y<= getHeight()-2) {
						boolean onright_ori = onright;
						onright = true;
						if (onright_ori != onright) {//大大减少了repaint()的调用次数
							setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
							repaint();
						}
					}else {
						boolean onleft_ori = onleft;
						boolean onright_ori = onright;
						onright = false;
						onleft = false;
						if (onleft_ori != onleft || onright_ori != onright) {//大大减少了repaint()的调用次数
							setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
							repaint();
						}
					}
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				if (editing) {
					return;
				}
				draging = true;
				if (onright) {//----------------------------RIGHT RESIZE-----------------------------
					if (temp_width+(e.getX()-sx) < 2*resize_anchor+3) {
						return;//限定块最小宽度
					}
					boolean snap = false;
					if (Timeline.getTimeline().allow_snap) {
						HRPane oneblock = null;
						for (int i = 0; i < Timeline.getTimeline().getBlocks().size(); i++) {
							//要避免吸附自己
							oneblock = Timeline.getTimeline().getBlocks().get(i);
							if (oneblock.getName().equals(getName())) {
								continue;
							}
							int endx = oneblock.getX()+oneblock.getWidth();
							if (Math.abs(getX()+temp_width+(e.getX()-sx)-oneblock.getX()) < Timeline.getTimeline().snap_factor) {
								if (oneblock.getX()-getX() < 2*resize_anchor+3) {
									break;//与最小宽度冲突则不吸附
								}
								setSize(oneblock.getX()-getX(), getHeight());
								snap = true;
								usesnapvalue = true;
								snap_to_value = oneblock.time_start;
								break;
							}
							if (Math.abs(getX()+temp_width+(e.getX()-sx)-endx) < Timeline.getTimeline().snap_factor) {
								if (endx-getX() < 2*resize_anchor+3) {
									break;//与最小宽度冲突则不吸附
								}
								setSize(endx-getX(), getHeight());
								snap = true;
								usesnapvalue = true;
								snap_to_value = oneblock.time_end;
								break;//---
							}
						}
					}
					if (!snap) {
						setSize(temp_width+(e.getX()-sx), getHeight());
						usesnapvalue = false;
					}
					drag_type = 2;
				}else if (onleft) {//----------------------------LEFT RESIZE-----------------------------
					if (getWidth()-(e.getX()-sx) < 2*resize_anchor+3) {
						return;//限定块最小宽度
					}
					boolean snap = false;
					if (Timeline.getTimeline().allow_snap) {
						HRPane oneblock = null;
						for (int i = 0; i < Timeline.getTimeline().getBlocks().size(); i++) {
							//要避免吸附自己
							oneblock = Timeline.getTimeline().getBlocks().get(i);
							if (oneblock.getName().equals(getName())) {
								continue;
							}
							int endx = oneblock.getX()+oneblock.getWidth();
							if (Math.abs(getX()+(e.getX()-sx)-endx) < Timeline.getTimeline().snap_factor) {
								if (temp_blockend-(oneblock.getX()+oneblock.getWidth()) < 2*resize_anchor+3) {
									break;//与最小宽度冲突则不吸附
								}
								setSize(temp_blockend-(oneblock.getX()+oneblock.getWidth()), getHeight());
								setLocation(oneblock.getX()+oneblock.getWidth(), getY());
								snap = true;
								usesnapvalue = true;
								snap_to_value = oneblock.time_end;
								break;
							}
							if (Math.abs(getX()+(e.getX()-sx)-oneblock.getX()) < Timeline.getTimeline().snap_factor) {
								if (temp_blockend-oneblock.getX() < 2*resize_anchor+3) {
									break;//与最小宽度冲突则不吸附
								}
								setSize(temp_blockend-oneblock.getX(), getHeight());
								setLocation(oneblock.getX(), getY());
								snap = true;
								usesnapvalue = true;
								snap_to_value = oneblock.time_start;
								break;//---
							}
						}
					}
					if (!snap) {
						setSize(getWidth()-(e.getX()-sx), getHeight());
						setLocation(getX()+(e.getX()-sx), getY());
						usesnapvalue = false;
					}
					drag_type = 1;
				}else {//----------------------------MOVE-----------------------------
					//-------------------换层------------------
					if (Math.abs(e.getY()-sy) > getHeight()) {
						if (e.getY()-sy > 0) {
							if (layer < Timeline.getTimeline().track_max) {
								//确保不超出设定的最大轨道数
								setLocation(getX(), temp_y + Timeline.getTimeline().track_hight);
								temp_y += Timeline.getTimeline().track_hight;
								layer ++;
							}
						}else {
							if (getY()-1 != Timeline.getTimeline().ruler_hight) {
								setLocation(getX(), temp_y - Timeline.getTimeline().track_hight);
								temp_y -= Timeline.getTimeline().track_hight;
								layer --;
							}
						}
					}
					if (TimelinePane.getTLPane().shiftdrag) {
						return;
					}
					//-------------------移动------------------------
					if (getX()+(e.getX()-sx) < 1) {//不超出左侧边界
						return;
					}
					if (Math.abs(getX()+(e.getX()-sx)) < Timeline.getTimeline().snap_factor) {//吸附左侧边界
						setLocation(1, getY());
						return;
					}
					//-----------------吸附------------------
					boolean snap = false;
					if (Timeline.getTimeline().allow_snap) {
						HRPane oneblock = null;
						for (int i = 0; i < Timeline.getTimeline().getBlocks().size(); i++) {
							//要避免吸附自己
							oneblock = Timeline.getTimeline().getBlocks().get(i);
							if (oneblock.getName().equals(getName())) {
								continue;
							}
							int endx = oneblock.getX()+oneblock.getWidth();
							if (Math.abs(getX()+(e.getX()-sx)-endx) < Timeline.getTimeline().snap_factor) {
								setLocation(oneblock.getX()+oneblock.getWidth(), getY());
								snap = true;
								usesnapvalue = true;
								snap_to_value = oneblock.time_end;
								break;
							}
							if (Math.abs(getX()+(e.getX()-sx)-oneblock.getX()) < Timeline.getTimeline().snap_factor) {
								setLocation(oneblock.getX(), getY());
								snap = true;
								usesnapvalue = true;
								snap_to_value = oneblock.time_start;
								break;//---
							}
						}
					}
					if (!snap) {
						setLocation(getX()+(e.getX()-sx), getY());
						usesnapvalue = false;
					}
					drag_type = 3;
					
				}
				
			}
		});
		
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseEntered(MouseEvent e) {
				on = true;
				repaint();
			}
			@Override
			public void mouseExited(MouseEvent e) {
				if (pressed) {
					return;
				}
				on = false;
				onleft = false;
				onright = false;
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				repaint();
			}
			@Override
			public void mousePressed(MouseEvent e){
				if (editing) {
					return;
				}
				pressed = true;
				sx = e.getX();temp_x = getX();temp_width = getWidth();
				sy = e.getY();temp_y = getY();
				temp_blockend = getX() + getWidth();
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e){
				if (editing) {
					return;
				}
				//更新边界时间信息
				switch (drag_type) {//1-左侧缩放  2-右侧缩放 3-移动
				case 1://
					if (usesnapvalue) {
						time_start = snap_to_value;
					}else {
						time_start = TimelinePane.getTLPane().x_to_time(getX());
					}
					break;
				case 2:
					if (usesnapvalue) {
						time_end = snap_to_value;
					}else {
						time_end = TimelinePane.getTLPane().x_to_time(getX()+getWidth());
					}
					break;
				case 3:
					long len = time_end-time_start;
					if (usesnapvalue) {
						time_start = snap_to_value;
					}else {
						time_start = TimelinePane.getTLPane().x_to_time(getX());
					}
					time_end = time_start+len;
					break;
				default:break;
				}	
				//如果选中的话，调整完长度，还要更新重复播放的区间
				if (chosen) {
					Timeline.getTimeline().repeat_start = time_start;
					Timeline.getTimeline().repeat_end = time_end;
				}
				//退出拖拽状态
				pressed = false;
				draging = false;
				ex = e.getX();
				ey = e.getY();
				onleft = false;
				onright = false;
				if (!(e.getX() >= 0 && e.getX()<= getWidth() && e.getY() >= 0 && e.getY() <= getHeight())) {
					on = false;
				}
				setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				repaint();
				if (Timeline.getTimeline().getBlocks().size() != 0) {//如果界面上没有东西，则不生成文件
					Timeline.getTimeline().EncodeSrt();
					TimelineX.getTimelineX().saveSrtFile();
					TimelineX.tlx.getPlayPane().getMediaPlayer().setSubTitleFile(
							TimelineX.getTimelineX().videofile.substring(0,TimelineX.getTimelineX().videofile.lastIndexOf("."))+".srt");
				}
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				requestFocus();
				TimelinePane.getTLPane().selected_layer = layer;
				TimelinePane.getTLPane().repaint();
				if (e.getClickCount() == 2) {
					edittext.setVisible(true);
					text.setVisible(false);
					edittext.requestFocusInWindow();
					temp_width = getWidth();
					setSize(600, getHeight());
					editing = true;
					repaint();
				}
			}
		});
		
		this.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				chosen = false;
				Timeline.getTimeline().repeat_start = -1;
				Timeline.getTimeline().repeat_end = -1;
				repaint();
			}
			@Override
			public void focusGained(FocusEvent e) {
				chosen = true;
				Timeline.getTimeline().repeat_start = time_start;
				Timeline.getTimeline().repeat_end = time_end;
				repaint();
			}
		});
		this.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				TimelinePane.getTLPane().shiftdrag = false;
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				//System.out.println(k);
				if (e.isShiftDown()) {//垂直拖放
					TimelinePane.getTLPane().shiftdrag = true;
				}
				if (k == 8 || k == 127) {//删除键
					Timeline.getTimeline().remove_block(getName());
				}else if (k == 10) {//回车键，进行文字编辑
					edittext.setVisible(true);
					text.setVisible(false);
					edittext.requestFocusInWindow();
					temp_width = getWidth();
					setSize(600, getHeight());
					editing = true;
					repaint();
				}else if (k == 27) {//ESC键，丢弃焦点，将焦点转移到时间轴
					TimelinePane.getTLPane().requestFocusInWindow();
				}else if (k == 32) {//SPACE
					TimelineX.getTimelineX().play();
				}else if (k == 49 && e.isAltDown()) {//分配到第1组（数字1键）
					group = 1;repaint();
				}else if (k == 50 && e.isAltDown()) {//分配到第2组（数字2键）
					group = 2;repaint();
				}else if (k == 51 && e.isAltDown()) {//分配到第3组（数字3键）
					group = 3;repaint();
				}else if (k == 52 && e.isAltDown()) {//分配到第4组（数字4键）
					group = 4;repaint();
				}else if (k == 53 && e.isAltDown()) {//分配到第5组（数字5键）
					group = 5;repaint();
				}
			}
		});
		edittext.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				
			}
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void keyPressed(KeyEvent e) {
				int k = e.getKeyCode();
				//System.out.println(k);
				if (k == 10) {//按下回车退出块文字编辑状态
					text.setText(edittext.getText());
					setToolTipText(edittext.getText());
					edittext.setVisible(false);
					text.setVisible(true);
					setSize(temp_width, getHeight());
					editing = false;
					if (e.isControlDown()) {//按下Ctl的话按回车在下面产生新的块
						System.out.println("duplicate:"+getName());
						Timeline.getTimeline().add_block_below(getName());
					}else {
						requestFocusInWindow();
					}
					repaint();
					if (Timeline.getTimeline().getBlocks().size() != 0) {//如果界面上没有东西，则不生成文件
						Timeline.getTimeline().EncodeSrt();
						TimelineX.getTimelineX().saveSrtFile();
						TimelineX.tlx.getPlayPane().getMediaPlayer().setSubTitleFile(
								TimelineX.getTimelineX().videofile.substring(0,TimelineX.getTimelineX().videofile.lastIndexOf("."))+".srt");
					}
				}
			}
		});
		edittext.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent arg0) {
				//文本框失去焦点时退出文字编辑状态，并且让块获取焦点
				text.setText(edittext.getText());
				setToolTipText(edittext.getText());
				edittext.setVisible(false);
				text.setVisible(true);
				setSize(temp_width, getHeight());
				editing = false;
				repaint();
				//requestFocusInWindow();
			}
			@Override
			public void focusGained(FocusEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
}
