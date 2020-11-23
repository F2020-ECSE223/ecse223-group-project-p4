package ca.mcgill.ecse223.flexibook.view;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFormattedTextField.AbstractFormatter;

public class DateLabelFormatter extends AbstractFormatter{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1309345802075312462L;
	
	private String pattern = "yyyy-MM-dd";
	private SimpleDateFormat formatter = new SimpleDateFormat(pattern);
	
	@Override
	public Object stringToValue(String text) throws ParseException {
	
		return formatter.parseObject(text);
	}
	@Override
	public String valueToString(Object value) throws ParseException {
		
		if(value != null) {
			Calendar calendar = (Calendar) value;
			return formatter.format(calendar.getTime());
		}
		return "";
	}
	
}