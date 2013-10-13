package views;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
//import java.awt.Polygon;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

//import com.sun.awt.AWTUtilities;




import modles.Timeline;
import controls.DispTime;
import controls.TimelineX;

/**
 *
 * @author  __USER__
 */
public class XWindow extends javax.swing.JFrame implements DropTargetListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private EmbeddedMediaPlayerComponentX playpane;
	int sx, sy;

	/** Creates new form XWindow */
	public XWindow() {
		playpane = new EmbeddedMediaPlayerComponentX();
		initComponents();
		playpancontainer.setLayout(new BorderLayout());
		playpancontainer.add(playpane, BorderLayout.CENTER);
		/*int[] pxs = { 2, 703, 703, 920, 920, 2 };
		int[] pys = { 30, 30, 0, 0, 607, 607 };
		Polygon shape = new Polygon(pxs, pys, 6);
		AWTUtilities.setWindowShape(this, shape);*/
		new DispTime(showtime).start();
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
	}

	//GEN-BEGIN:initComponents
	// <editor-fold defaultstate="collapsed" desc="Generated Code">
	private void initComponents() {

		toppane = new javax.swing.JPanel();
		jScrollPane1 = new JScrollPane(TimelinePane.getTLPane());
		showtime = new javax.swing.JLabel();
		jButton1 = new javax.swing.JButton();
		jLabel1 = new javax.swing.JLabel();
		playpancontainer = new javax.swing.JPanel();
		exit = new javax.swing.JLabel();
		minimize = new javax.swing.JLabel();
		volume = new javax.swing.JSlider();
		jButton2 = new javax.swing.JButton();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		setBackground(java.awt.SystemColor.controlDkShadow);
		setMinimumSize(new java.awt.Dimension(720, 480));
		setUndecorated(true);
		addWindowListener(new java.awt.event.WindowAdapter() {
			public void windowClosing(java.awt.event.WindowEvent evt) {
				formWindowClosing(evt);
			}
		});

		toppane.setBackground(new java.awt.Color(50, 50, 50));
		toppane.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mousePressed(java.awt.event.MouseEvent evt) {
				toppaneMousePressed(evt);
			}
		});
		toppane.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				toppaneMouseDragged(evt);
			}
		});

		jScrollPane1.setBorder(javax.swing.BorderFactory
				.createLineBorder(new java.awt.Color(255, 102, 0)));
		jScrollPane1
				.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

		showtime.setFont(new java.awt.Font("微软雅黑", 0, 14));
		showtime.setForeground(new java.awt.Color(255, 153, 0));
		showtime.setText("0s");

		jButton1.setText("ADD BLOCK");
		jButton1.setFocusable(false);
		jButton1.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton1ActionPerformed(evt);
			}
		});

		jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/logo.png"))); // NOI18N

		playpancontainer.setBorder(javax.swing.BorderFactory.createLineBorder(
				new java.awt.Color(27, 27, 27), 2));

		javax.swing.GroupLayout playpancontainerLayout = new javax.swing.GroupLayout(
				playpancontainer);
		playpancontainer.setLayout(playpancontainerLayout);
		playpancontainerLayout.setHorizontalGroup(playpancontainerLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 789, Short.MAX_VALUE));
		playpancontainerLayout.setVerticalGroup(playpancontainerLayout
				.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addGap(0, 444, Short.MAX_VALUE));

		exit.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/x.png"))); // NOI18N
		exit.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				exitMouseEntered(evt);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				exitMouseExited(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				exitMouseReleased(evt);
			}
		});

		minimize.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/min.png"))); // NOI18N
		minimize.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseEntered(java.awt.event.MouseEvent evt) {
				minimizeMouseEntered(evt);
			}

			public void mouseExited(java.awt.event.MouseEvent evt) {
				minimizeMouseExited(evt);
			}

			public void mouseReleased(java.awt.event.MouseEvent evt) {
				minimizeMouseReleased(evt);
			}
		});

		volume.setFocusable(false);
		volume.setOpaque(false);
		volume.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
			public void mouseDragged(java.awt.event.MouseEvent evt) {
				volumeMouseDragged(evt);
			}
		});

		jButton2.setText("CLEAR");
		jButton2.setFocusable(false);
		jButton2.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButton2ActionPerformed(evt);
			}
		});

		javax.swing.GroupLayout toppaneLayout = new javax.swing.GroupLayout(
				toppane);
		toppane.setLayout(toppaneLayout);
		toppaneLayout
				.setHorizontalGroup(toppaneLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								toppaneLayout
										.createSequentialGroup()
										.addContainerGap()
										.addGroup(
												toppaneLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																toppaneLayout
																		.createSequentialGroup()
																		.addComponent(
																				playpancontainer,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
																		.addGroup(
																				toppaneLayout
																						.createParallelGroup(
																								javax.swing.GroupLayout.Alignment.LEADING)
																						.addGroup(
																								toppaneLayout
																										.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.LEADING)
																										.addGroup(
																												javax.swing.GroupLayout.Alignment.TRAILING,
																												toppaneLayout
																														.createSequentialGroup()
																														.addComponent(
																																minimize)
																														.addPreferredGap(
																																javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																														.addComponent(
																																exit))
																										.addComponent(
																												jLabel1))
																						.addGroup(
																								toppaneLayout
																										.createParallelGroup(
																												javax.swing.GroupLayout.Alignment.TRAILING,
																												false)
																										.addComponent(
																												jButton2,
																												javax.swing.GroupLayout.Alignment.LEADING,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE)
																										.addComponent(
																												jButton1,
																												javax.swing.GroupLayout.Alignment.LEADING,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												javax.swing.GroupLayout.DEFAULT_SIZE,
																												Short.MAX_VALUE))))
														.addComponent(
																jScrollPane1,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																1005,
																Short.MAX_VALUE)
														.addGroup(
																toppaneLayout
																		.createSequentialGroup()
																		.addComponent(
																				showtime,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				134,
																				javax.swing.GroupLayout.PREFERRED_SIZE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				671,
																				Short.MAX_VALUE)
																		.addComponent(
																				volume,
																				javax.swing.GroupLayout.PREFERRED_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.PREFERRED_SIZE)))
										.addContainerGap()));
		toppaneLayout
				.setVerticalGroup(toppaneLayout
						.createParallelGroup(
								javax.swing.GroupLayout.Alignment.LEADING)
						.addGroup(
								toppaneLayout
										.createSequentialGroup()
										.addContainerGap()
										.addComponent(jLabel1)
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addGroup(
												toppaneLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.LEADING)
														.addGroup(
																toppaneLayout
																		.createSequentialGroup()
																		.addComponent(
																				exit)
																		.addGap(309,
																				309,
																				309)
																		.addComponent(
																				jButton2)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED)
																		.addComponent(
																				jButton1)
																		.addGap(59,
																				59,
																				59))
														.addGroup(
																toppaneLayout
																		.createSequentialGroup()
																		.addComponent(
																				playpancontainer,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				javax.swing.GroupLayout.DEFAULT_SIZE,
																				Short.MAX_VALUE)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED))
														.addGroup(
																toppaneLayout
																		.createSequentialGroup()
																		.addComponent(
																				minimize)
																		.addPreferredGap(
																				javax.swing.LayoutStyle.ComponentPlacement.RELATED,
																				426,
																				Short.MAX_VALUE)))
										.addGap(8, 8, 8)
										.addGroup(
												toppaneLayout
														.createParallelGroup(
																javax.swing.GroupLayout.Alignment.TRAILING,
																false)
														.addComponent(volume,
																0, 0,
																Short.MAX_VALUE)
														.addComponent(
																showtime,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																javax.swing.GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE))
										.addPreferredGap(
												javax.swing.LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(
												jScrollPane1,
												javax.swing.GroupLayout.PREFERRED_SIZE,
												170,
												javax.swing.GroupLayout.PREFERRED_SIZE)
										.addContainerGap()));

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(
				getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				toppane, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(
				javax.swing.GroupLayout.Alignment.LEADING).addComponent(
				toppane, javax.swing.GroupLayout.DEFAULT_SIZE,
				javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE));

		pack();
	}// </editor-fold>
	//GEN-END:initComponents

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
		Timeline.getTimeline().clearAll();
	}

	private void volumeMouseDragged(java.awt.event.MouseEvent evt) {
		TimelineX.getTimelineX().setVolume(volume.getValue());
	}

	private void minimizeMouseReleased(java.awt.event.MouseEvent evt) {
		setExtendedState(JFrame.ICONIFIED);
	}

	private void minimizeMouseExited(java.awt.event.MouseEvent evt) {
		minimize.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/min.png"))); // NOI18N
	}

	private void minimizeMouseEntered(java.awt.event.MouseEvent evt) {
		minimize.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/min_on.png"))); // NOI18N
	}

	private void exitMouseReleased(java.awt.event.MouseEvent evt) {
		playpane.getMediaPlayer().stop();
		playpane.release();
		System.exit(0);
	}

	private void exitMouseExited(java.awt.event.MouseEvent evt) {
		exit.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/x.png"))); // NOI18N
	}

	private void exitMouseEntered(java.awt.event.MouseEvent evt) {
		exit.setIcon(new javax.swing.ImageIcon(getClass().getResource(
				"/views/x_on.png"))); // NOI18N
	}

	private void toppaneMouseDragged(java.awt.event.MouseEvent e) {
		setLocation(getX() + (e.getX() - sx), getY() + (e.getY() - sy));
	}

	private void toppaneMousePressed(java.awt.event.MouseEvent e) {
		sx = e.getX();
		sy = e.getY();
	}

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
		Timeline.getTimeline().add_block(new HRPane(""), 0, true);
	}

	private void formWindowClosing(java.awt.event.WindowEvent evt) {
		playpane.getMediaPlayer().stop();
		playpane.release();
	}

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new XWindow().setVisible(true);
			}
		});
	}

	public EmbeddedMediaPlayerComponentX getPlayPane() {
		return playpane;
	}

	//GEN-BEGIN:variables
	// Variables declaration - do not modify
	private javax.swing.JLabel exit;
	private javax.swing.JButton jButton1;
	private javax.swing.JButton jButton2;
	private javax.swing.JLabel jLabel1;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JLabel minimize;
	private javax.swing.JPanel playpancontainer;
	public javax.swing.JLabel showtime;
	private javax.swing.JPanel toppane;
	private javax.swing.JSlider volume;
	// End of variables declaration//GEN-END:variables

	@Override
	public void dragEnter(DropTargetDragEvent arg0) {
		setCursor(new Cursor(Cursor.MOVE_CURSOR));
	}

	@Override
	public void dragExit(DropTargetEvent arg0) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void dragOver(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drop(DropTargetDropEvent arg0) {
		setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		TimelineX.getTimelineX().readDroppedFile(arg0);
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}