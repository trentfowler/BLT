package DLT;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import org.joda.time.LocalDate;


public class RightClickList extends JPopupMenu {

	private static final long serialVersionUID = -1474472275653429868L;
	
	private JMenuItem open;
	private JMenuItem close;
	private JMenuItem touch; 
	private UtilDateModel model = new UtilDateModel();
	private JDatePanelImpl datePanel = new JDatePanelImpl(model);
	private JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
	
	public RightClickList() {
		//initialize components
		this.setOpaque(true);
		this.setBackground(Color.WHITE);
		this.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		close = new JMenuItem("Close case");
		close.setOpaque(true);
		close.setBackground(Color.WHITE);
		open = new JMenuItem("Open case");
		touch = new JMenuItem("Touch case");
		open.setOpaque(true);
		open.setBackground(Color.WHITE);
		if (Main.FIELDS.get(Main.SELECTED_INDEX).getStatus() != Main.STATUS_IS_CLOSED) {
			this.model.setDate(Main.FIELDS.get(Main.SELECTED_INDEX).getCommittedDate().getYear(),
								Main.FIELDS.get(Main.SELECTED_INDEX).getCommittedDate().getMonthOfYear() - 1,
								Main.FIELDS.get(Main.SELECTED_INDEX).getCommittedDate().getDayOfMonth());
			this.model.setSelected(true);
		}
		
		//add components
		if (Main.FIELDS.get(Main.SELECTED_INDEX).getStatus() == Main.STATUS_IS_CLOSED ||
			Main.FIELDS.get(Main.SELECTED_INDEX).getStatus() == Main.STATUS_IS_UNKNOWN)
			this.add(open);
		else{ this.add(touch);
			  this.addSeparator();
			  this.add(datePicker);
			  this.add(close);
		}
		
		
		//add listeners
		//touch selected
		touch.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				LocalDate today = new LocalDate();
				Main.FIELDS.get(Main.SELECTED_INDEX).setCommittedDate(today.plusDays(1));
				Main.FIELDS.get(Main.SELECTED_INDEX).setStatus(Main.STATUS_IS_TOUCHED);
				Main.HAS_UNSAVED_CHANGES = true;
			}
		});
				
		//open selected
		open.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				LocalDate today = new LocalDate();
				Main.FIELDS.get(Main.SELECTED_INDEX).setCommittedDate(today.plusDays(1));
				Main.FIELDS.get(Main.SELECTED_INDEX).setStatus(Main.STATUS_IS_TOUCHED);
				Main.HAS_UNSAVED_CHANGES = true;
			}
		});
		
		//close selected
		close.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Main.FIELDS.get(Main.SELECTED_INDEX).setStatus(Main.STATUS_IS_CLOSED);
				Main.HAS_UNSAVED_CHANGES = true;
			}
		});
		
		//datepicker selected
		datePicker.addActionListener(new ActionListener() {
			@Override public void actionPerformed(ActionEvent e) {
				Date date = (Date) datePicker.getModel().getValue();
				Main.FIELDS.get(Main.SELECTED_INDEX).setCommittedDate(new LocalDate(date));
				LocalDate today = new LocalDate();
				if (Main.FIELDS.get(Main.SELECTED_INDEX).getCommittedDate().isAfter(today)) {
					Main.FIELDS.get(Main.SELECTED_INDEX).setStatus(Main.STATUS_IS_TOUCHED);
				}
				
				else if (Main.FIELDS.get(Main.SELECTED_INDEX).getCommittedDate().isBefore(today)) {
					Main.FIELDS.get(Main.SELECTED_INDEX).setStatus(Main.STATUS_IS_OVERDUE);
				}
				
				else if (Main.FIELDS.get(Main.SELECTED_INDEX).getCommittedDate().equals(today)) {
					Main.FIELDS.get(Main.SELECTED_INDEX).setStatus(Main.STATUS_IS_DUE);
				}
				Main.HAS_UNSAVED_CHANGES = true;
			}
		});
	}	
}
