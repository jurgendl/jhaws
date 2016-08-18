package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

import net.miginfocom.swing.MigLayout;

/**
 * @author Jurgen
 */
public class EDateTimeChooser extends JPanel {
    private static final long serialVersionUID = 4865835185479753960L;

    public static String[] getDayStrings(Locale l) {
        String[] _days = new java.text.DateFormatSymbols(l).getShortWeekdays();
        int lastIndex = _days.length - 1;

        if (_days[lastIndex] == null || _days[lastIndex].length() <= 0) {
            // last item empty
            String[] monthStrings = new String[lastIndex];
            System.arraycopy(_days, 0, monthStrings, 0, lastIndex);
            return monthStrings;
        }
        // last item not empty
        return _days;
    }

    /**
     * DateFormatSymbols returns an extra, empty value at the end of the array of months. Remove it.
     */
    public static String[] getMonthStrings(Locale l) {
        String[] _months = new java.text.DateFormatSymbols(l).getMonths();
        int lastIndex = _months.length - 1;

        if (_months[lastIndex] == null || _months[lastIndex].length() <= 0) {
            // last item empty
            String[] monthStrings = new String[lastIndex];
            System.arraycopy(_months, 0, monthStrings, 0, lastIndex);
            return monthStrings;
        }
        // last item not empty
        return _months;
    }

    protected boolean verbose = false;

    protected List<String> dayStrings;

    protected Calendar calendar;

    protected JPanel spinnerPanel;

    protected List<String> monthStrings;

    protected JPanel dayPanel;

    protected ESpinner<String> monthSpinner;

    protected ESpinner<Integer> yearSpinner;

    protected ELabel yearLabel;

    protected ELabel monthLabel;

    protected boolean valueChanging = false;

    protected ChangeListener spinnerChangelistener;

    protected ESpinnerCyclingModelListener spinnerCyclingModelListener;

    protected ESpinner<Date> timeSpinner;

    protected ELabel timeLabel;

    protected final DateTimeType type;

    public EDateTimeChooser() {
        this(new Date(), DateTimeType.DATE_TIME);
    }

    public EDateTimeChooser(Date date, DateTimeType type) {
        this(date, Locale.getDefault(), type);
    }

    public EDateTimeChooser(Date date, Locale locale, DateTimeType type) {
        this(date, locale, TimeZone.getDefault(), type);
    }

    public EDateTimeChooser(Date date, Locale locale, TimeZone tz, DateTimeType type) {
        this.type = type;
        setLocale(locale);
        calendar = new GregorianCalendar(tz, locale);
        calendar.setTime(date);

        buildComponent();
    }

    public EDateTimeChooser(DateTimeType type) {
        this(new Date(), type);
    }

    protected void buildComponent() {
        setLayout(new BorderLayout());

        this.add(getSpinnersPanel(), BorderLayout.NORTH);
        this.add(getDayPanel(), BorderLayout.CENTER);

        rebuildDateSelectionComponent();
    }

    public Date getDate() {
        return calendar.getTime();
    }

    protected JPanel getDayPanel() {
        if (dayPanel == null) {
            if (type == DateTimeType.TIME) {
                dayPanel = new JPanel();
            } else {
                dayPanel = new JPanel(new MigLayout("wrap 7", "[center]2px[center]2px[center]2px[center]2px[center]2px[center]2px[center]")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return dayPanel;
    }

    protected ELabel getMonthLabel() {
        if (monthLabel == null) {
            monthLabel = new ELabel();
        }
        return monthLabel;
    }

    protected ESpinner<String> getMonthSpinner() {
        if (monthSpinner == null) {
            ESpinnerCyclingModel<String> monthModel = new ESpinnerCyclingModel<>(monthStrings);
            monthModel.addCyclingSpinnerListModelListener(getSpinnerCyclingModelListener());
            monthSpinner = new ESpinner<>(monthModel);
            monthSpinner.setMinimumSize(new Dimension(110, 1));
            monthModel.addChangeListener(getSpinnerChangelistener());
        }
        return monthSpinner;
    }

    protected ChangeListener getSpinnerChangelistener() {
        if (spinnerChangelistener == null) {
            spinnerChangelistener = e -> {
                if (!valueChanging) {
                    int yy = (Integer) EDateTimeChooser.this.getYearSpinner().getValue();
                    int mm = monthStrings.indexOf(EDateTimeChooser.this.getMonthSpinner().getValue());
                    int dd = calendar.get(Calendar.DAY_OF_MONTH);
                    int hh = calendar.get(Calendar.HOUR_OF_DAY);
                    int mi = calendar.get(Calendar.MINUTE);
                    int ss = calendar.get(Calendar.SECOND);
                    EDateTimeChooser.this.setDateTime(yy, mm, dd, hh, mi, ss);
                }
            };
        }
        return spinnerChangelistener;
    }

    protected ESpinnerCyclingModelListener getSpinnerCyclingModelListener() {
        if (spinnerCyclingModelListener == null) {
            spinnerCyclingModelListener = new ESpinnerCyclingModelListener() {
                @Override
                public void overflow() {
                    @SuppressWarnings("unchecked")
                    ESpinnerCyclingModel<Integer> model = ESpinnerCyclingModel.class.cast(EDateTimeChooser.this.getYearSpinner().getModel());
                    model.setValue(model.getNextValue());
                }

                @Override
                public void rollback() {
                    @SuppressWarnings("unchecked")
                    ESpinnerCyclingModel<Integer> model = ESpinnerCyclingModel.class.cast(EDateTimeChooser.this.getYearSpinner().getModel());
                    model.setValue(model.getPreviousValue());
                }
            };
        }
        return spinnerCyclingModelListener;
    }

    protected JPanel getSpinnersPanel() {
        if (spinnerPanel == null) {
            spinnerPanel = new JPanel(new MigLayout("", "[]rel[]10px[]rel[]"));

            boolean addDate = false;
            boolean addTime = false;
            switch (type) {
                case DATE:
                    addDate = true;
                    break;
                case TIME:
                    addTime = true;
                    break;
                case DATE_TIME:
                default:
                    addDate = true;
                    addTime = true;
                    break;
            }

            if (addTime) {
                spinnerPanel.add(getTimeLabel(), "");
                spinnerPanel.add(getTimeSpinner(), "grow, wrap");
            }

            if (addDate) {
                spinnerPanel.add(getMonthLabel(), "");
                spinnerPanel.add(getMonthSpinner(), "grow");
                spinnerPanel.add(getYearLabel(), "");
                spinnerPanel.add(getYearSpinner(), "grow");
            }
        }
        return spinnerPanel;
    }

    protected ELabel getTimeLabel() {
        if (timeLabel == null) {
            timeLabel = new ELabel();
        }
        return timeLabel;
    }

    protected ESpinner<Date> getTimeSpinner() {
        if (timeSpinner == null) {
            timeSpinner = new ESpinner<>(new SpinnerDateModel());
            ESpinner.DateEditor timeEditor = new ESpinner.DateEditor(timeSpinner, "HH:mm");
            timeSpinner.setEditor(timeEditor);
        }
        return timeSpinner;
    }

    protected ELabel getYearLabel() {
        if (yearLabel == null) {
            yearLabel = new ELabel();
        }
        return yearLabel;
    }

    protected ESpinner<Integer> getYearSpinner() {
        if (yearSpinner == null) {
            @SuppressWarnings("deprecation")
            SpinnerNumberModel yearModel = new SpinnerNumberModel(new Date().getYear() + 1900, 1, 9999, 1);
            yearSpinner = new ESpinner<>(yearModel);
            ESpinner.NumberEditor numberEditor = new ESpinner.NumberEditor(yearSpinner, "0000");
            yearSpinner.setEditor(numberEditor);
            yearSpinner.setMinimumSize(new Dimension(50, 1));
            yearModel.addChangeListener(getSpinnerChangelistener());
        }
        return yearSpinner;
    }

    protected void log(Object msg) {
        if (verbose) {
            System.out.println(msg);
        }
    }

    protected void rebuildDateSelectionComponent() {
        if (type == DateTimeType.TIME) {
            return;
        }

        log(calendar.getTime());
        log("FIRST D/O/W " + calendar.getFirstDayOfWeek()); //$NON-NLS-1$
        log("Y " + calendar.get(Calendar.YEAR)); //$NON-NLS-1$
        log("M " + calendar.get(Calendar.MONTH)); //$NON-NLS-1$
        log("D " + calendar.get(Calendar.DAY_OF_MONTH)); //$NON-NLS-1$

        getMonthSpinner().setValue(monthStrings.get(calendar.get(Calendar.MONTH)));
        getYearSpinner().setValue(calendar.get(Calendar.YEAR));

        getDayPanel().removeAll();

        int x = 0;

        for (int i = calendar.getFirstDayOfWeek(); i < calendar.getFirstDayOfWeek() + 7; i++) {
            int j = i - 1;
            if (j >= dayStrings.size()) {
                j -= dayStrings.size();
            }
            x++;
            getDayPanel().add(new ELabel(dayStrings.get(j)), ""); //$NON-NLS-1$
        }

        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        log(calendar.getTime());
        log("Y " + calendar.get(Calendar.YEAR)); //$NON-NLS-1$
        log("M " + calendar.get(Calendar.MONTH)); //$NON-NLS-1$
        log("D " + calendar.get(Calendar.DAY_OF_MONTH)); //$NON-NLS-1$
        log("CURRENT D/O/W " + calendar.get(Calendar.DAY_OF_WEEK)); //$NON-NLS-1$

        int empty = calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek();
        if (empty < 0) {
            empty += 7;
        }
        log("empty " + empty); //$NON-NLS-1$

        for (int i = 0; i < empty; i++) {
            x++;
            JLabel el = new JLabel(""); //$NON-NLS-1$
            getDayPanel().add(el, ""); //$NON-NLS-1$
        }

        ButtonGroup bg = new ButtonGroup();

        int maximum = calendar.getMaximum(Calendar.DAY_OF_MONTH);
        log("max: " + maximum); //$NON-NLS-1$

        Dimension defaultDimension = new Dimension(36, 20);
        int y = 0;
        for (int i = 0; i < maximum; i++) {
            final String id = String.valueOf(i + 1);
            EToggleButton comp = new EToggleButton(new EButtonConfig(new EToolBarButtonCustomizer(defaultDimension), id));
            comp.addActionListener(e -> calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(id)));
            comp.setMargin(new Insets(0, 0, 0, 0));
            comp.setOpaque(true);
            comp.setFont(getFont().deriveFont(8f));
            bg.add(comp);
            x++;
            if (x % 7 == 0 && x != 0) {
                y++;
                getDayPanel().add(comp, "wrap"); //$NON-NLS-1$
            } else {
                getDayPanel().add(comp, ""); //$NON-NLS-1$
            }

            if (i + 1 == dayOfMonth) {
                comp.setSelected(true);
            }
        }
        log("y " + y); //$NON-NLS-1$
        if (y == 4) {
            for (int i = 0; i < 7; i++) {
                JLabel el = new JLabel(""); //$NON-NLS-1$
                el.setMinimumSize(defaultDimension);
                getDayPanel().add(el, ""); //$NON-NLS-1$
            }
        }

        getDayPanel().revalidate();

        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public void setDate(Date date) {
        valueChanging = true;
        calendar.setTime(date);
        getTimeSpinner().setValue(date);
        rebuildDateSelectionComponent();
        updateValues();
        valueChanging = false;
    }

    public void setDate(int y, int m, int d) {
        valueChanging = true;
        setDateInternal(y, m, d);
        rebuildDateSelectionComponent();
        updateValues();
        valueChanging = false;
    }

    protected void setDateInternal(int y, int m, int d) {
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m);
        calendar.set(Calendar.DAY_OF_MONTH, Math.min(calendar.getMaximum(Calendar.DAY_OF_MONTH), d));
    }

    public void setDateTime(int y, int mo, int d, int h, int mi, int s) {
        valueChanging = true;
        setDateInternal(y, mo, d);
        setTimeInternal(h, mi, s);
        rebuildDateSelectionComponent();
        updateValues();
        valueChanging = false;
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        monthStrings = Arrays.asList(EDateTimeChooser.getMonthStrings(l));
        dayStrings = Arrays.asList(EDateTimeChooser.getDayStrings(l));
        getMonthLabel().setText(Messages.getString(l, "EDateTimeChooser.month"));
        getYearLabel().setText(Messages.getString(l, "EDateTimeChooser.year"));
        getTimeLabel().setText(Messages.getString(l, "EDateTimeChooser.time"));
    }

    public void setTime(int h, int m, int s) {
        valueChanging = true;
        setTimeInternal(h, m, s);
        rebuildDateSelectionComponent();
        updateValues();
        valueChanging = false;
    }

    protected void setTimeInternal(int h, int m, int s) {
        calendar.set(Calendar.HOUR_OF_DAY, h);
        calendar.set(Calendar.MINUTE, m);
        calendar.set(Calendar.SECOND, s);
    }

    protected void updateValues() {
        getMonthSpinner().setValue(monthStrings.get(calendar.get(Calendar.MONTH)));
        getYearSpinner().setValue(calendar.get(Calendar.YEAR));
        getTimeSpinner().setValue(calendar.getTime());
    }
}
