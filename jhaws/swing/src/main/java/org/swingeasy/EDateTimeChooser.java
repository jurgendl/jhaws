package org.swingeasy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import javax.swing.event.ChangeEvent;
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

        if ((_days[lastIndex] == null) || (_days[lastIndex].length() <= 0)) {
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

        if ((_months[lastIndex] == null) || (_months[lastIndex].length() <= 0)) {
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
        this.setLocale(locale);
        this.calendar = new GregorianCalendar(tz, locale);
        this.calendar.setTime(date);

        this.buildComponent();
    }

    public EDateTimeChooser(DateTimeType type) {
        this(new Date(), type);
    }

    protected void buildComponent() {
        this.setLayout(new BorderLayout());

        this.add(this.getSpinnersPanel(), BorderLayout.NORTH);
        this.add(this.getDayPanel(), BorderLayout.CENTER);

        this.rebuildDateSelectionComponent();
    }

    public Date getDate() {
        return this.calendar.getTime();
    }

    protected JPanel getDayPanel() {
        if (this.dayPanel == null) {
            if (this.type == DateTimeType.TIME) {
                this.dayPanel = new JPanel();
            } else {
                this.dayPanel = new JPanel(new MigLayout("wrap 7", "[center]2px[center]2px[center]2px[center]2px[center]2px[center]2px[center]")); //$NON-NLS-1$ //$NON-NLS-2$
            }
        }
        return this.dayPanel;
    }

    protected ELabel getMonthLabel() {
        if (this.monthLabel == null) {
            this.monthLabel = new ELabel();
        }
        return this.monthLabel;
    }

    protected ESpinner<String> getMonthSpinner() {
        if (this.monthSpinner == null) {
            ESpinnerCyclingModel<String> monthModel = new ESpinnerCyclingModel<String>(this.monthStrings);
            monthModel.addCyclingSpinnerListModelListener(this.getSpinnerCyclingModelListener());
            this.monthSpinner = new ESpinner<String>(monthModel);
            this.monthSpinner.setMinimumSize(new Dimension(110, 1));
            monthModel.addChangeListener(this.getSpinnerChangelistener());
        }
        return this.monthSpinner;
    }

    protected ChangeListener getSpinnerChangelistener() {
        if (this.spinnerChangelistener == null) {
            this.spinnerChangelistener = new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    if (!EDateTimeChooser.this.valueChanging) {
                        int yy = (Integer) EDateTimeChooser.this.getYearSpinner().getValue();
                        int mm = EDateTimeChooser.this.monthStrings.indexOf(EDateTimeChooser.this.getMonthSpinner().getValue());
                        int dd = EDateTimeChooser.this.calendar.get(Calendar.DAY_OF_MONTH);
                        int hh = EDateTimeChooser.this.calendar.get(Calendar.HOUR_OF_DAY);
                        int mi = EDateTimeChooser.this.calendar.get(Calendar.MINUTE);
                        int ss = EDateTimeChooser.this.calendar.get(Calendar.SECOND);
                        EDateTimeChooser.this.setDateTime(yy, mm, dd, hh, mi, ss);
                    }
                }
            };
        }
        return this.spinnerChangelistener;
    }

    protected ESpinnerCyclingModelListener getSpinnerCyclingModelListener() {
        if (this.spinnerCyclingModelListener == null) {
            this.spinnerCyclingModelListener = new ESpinnerCyclingModelListener() {
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
        return this.spinnerCyclingModelListener;
    }

    protected JPanel getSpinnersPanel() {
        if (this.spinnerPanel == null) {
            this.spinnerPanel = new JPanel(new MigLayout("", "[]rel[]10px[]rel[]"));

            boolean addDate = false;
            boolean addTime = false;
            switch (this.type) {
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
                this.spinnerPanel.add(this.getTimeLabel(), "");
                this.spinnerPanel.add(this.getTimeSpinner(), "grow, wrap");
            }

            if (addDate) {
                this.spinnerPanel.add(this.getMonthLabel(), "");
                this.spinnerPanel.add(this.getMonthSpinner(), "grow");
                this.spinnerPanel.add(this.getYearLabel(), "");
                this.spinnerPanel.add(this.getYearSpinner(), "grow");
            }
        }
        return this.spinnerPanel;
    }

    protected ELabel getTimeLabel() {
        if (this.timeLabel == null) {
            this.timeLabel = new ELabel();
        }
        return this.timeLabel;
    }

    protected ESpinner<Date> getTimeSpinner() {
        if (this.timeSpinner == null) {
            this.timeSpinner = new ESpinner<Date>(new SpinnerDateModel());
            ESpinner.DateEditor timeEditor = new ESpinner.DateEditor(this.timeSpinner, "HH:mm");
            this.timeSpinner.setEditor(timeEditor);
        }
        return this.timeSpinner;
    }

    protected ELabel getYearLabel() {
        if (this.yearLabel == null) {
            this.yearLabel = new ELabel();
        }
        return this.yearLabel;
    }

    protected ESpinner<Integer> getYearSpinner() {
        if (this.yearSpinner == null) {
            @SuppressWarnings("deprecation")
            SpinnerNumberModel yearModel = new SpinnerNumberModel(new Date().getYear() + 1900, 1, 9999, 1);
            this.yearSpinner = new ESpinner<Integer>(yearModel);
            ESpinner.NumberEditor numberEditor = new ESpinner.NumberEditor(this.yearSpinner, "0000");
            this.yearSpinner.setEditor(numberEditor);
            this.yearSpinner.setMinimumSize(new Dimension(50, 1));
            yearModel.addChangeListener(this.getSpinnerChangelistener());
        }
        return this.yearSpinner;
    }

    protected void log(Object msg) {
        if (this.verbose) {
            System.out.println(msg);
        }
    }

    protected void rebuildDateSelectionComponent() {
        if (this.type == DateTimeType.TIME) {
            return;
        }

        this.log(this.calendar.getTime());
        this.log("FIRST D/O/W " + this.calendar.getFirstDayOfWeek()); //$NON-NLS-1$
        this.log("Y " + this.calendar.get(Calendar.YEAR)); //$NON-NLS-1$
        this.log("M " + this.calendar.get(Calendar.MONTH)); //$NON-NLS-1$
        this.log("D " + this.calendar.get(Calendar.DAY_OF_MONTH)); //$NON-NLS-1$

        this.getMonthSpinner().setValue(this.monthStrings.get(this.calendar.get(Calendar.MONTH)));
        this.getYearSpinner().setValue(this.calendar.get(Calendar.YEAR));

        this.getDayPanel().removeAll();

        int x = 0;

        for (int i = this.calendar.getFirstDayOfWeek(); i < (this.calendar.getFirstDayOfWeek() + 7); i++) {
            int j = i - 1;
            if (j >= this.dayStrings.size()) {
                j -= this.dayStrings.size();
            }
            x++;
            this.getDayPanel().add(new ELabel(this.dayStrings.get(j)), ""); //$NON-NLS-1$
        }

        int dayOfMonth = this.calendar.get(Calendar.DAY_OF_MONTH);
        this.calendar.set(Calendar.DAY_OF_MONTH, 1);

        this.log(this.calendar.getTime());
        this.log("Y " + this.calendar.get(Calendar.YEAR)); //$NON-NLS-1$
        this.log("M " + this.calendar.get(Calendar.MONTH)); //$NON-NLS-1$
        this.log("D " + this.calendar.get(Calendar.DAY_OF_MONTH)); //$NON-NLS-1$
        this.log("CURRENT D/O/W " + this.calendar.get(Calendar.DAY_OF_WEEK)); //$NON-NLS-1$

        int empty = this.calendar.get(Calendar.DAY_OF_WEEK) - this.calendar.getFirstDayOfWeek();
        if (empty < 0) {
            empty += 7;
        }
        this.log("empty " + empty); //$NON-NLS-1$

        for (int i = 0; i < empty; i++) {
            x++;
            JLabel el = new JLabel(""); //$NON-NLS-1$
            this.getDayPanel().add(el, ""); //$NON-NLS-1$
        }

        ButtonGroup bg = new ButtonGroup();

        int maximum = this.calendar.getMaximum(Calendar.DAY_OF_MONTH);
        this.log("max: " + maximum); //$NON-NLS-1$

        Dimension defaultDimension = new Dimension(36, 20);
        int y = 0;
        for (int i = 0; i < maximum; i++) {
            final String id = String.valueOf((i + 1));
            EToggleButton comp = new EToggleButton(new EButtonConfig(new EToolBarButtonCustomizer(defaultDimension), id));
            comp.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    EDateTimeChooser.this.calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(id));
                }
            });
            comp.setMargin(new Insets(0, 0, 0, 0));
            comp.setOpaque(true);
            comp.setFont(this.getFont().deriveFont(8f));
            bg.add(comp);
            x++;
            if ((((x % 7) == 0) && (x != 0))) {
                y++;
                this.getDayPanel().add(comp, "wrap"); //$NON-NLS-1$
            } else {
                this.getDayPanel().add(comp, ""); //$NON-NLS-1$
            }

            if ((i + 1) == dayOfMonth) {
                comp.setSelected(true);
            }
        }
        this.log("y " + y); //$NON-NLS-1$
        if (y == 4) {
            for (int i = 0; i < 7; i++) {
                JLabel el = new JLabel(""); //$NON-NLS-1$
                el.setMinimumSize(defaultDimension);
                this.getDayPanel().add(el, ""); //$NON-NLS-1$
            }
        }

        this.getDayPanel().revalidate();

        this.calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
    }

    public void setDate(Date date) {
        this.valueChanging = true;
        this.calendar.setTime(date);
        this.getTimeSpinner().setValue(date);
        this.rebuildDateSelectionComponent();
        this.updateValues();
        this.valueChanging = false;
    }

    public void setDate(int y, int m, int d) {
        this.valueChanging = true;
        this.setDateInternal(y, m, d);
        this.rebuildDateSelectionComponent();
        this.updateValues();
        this.valueChanging = false;
    }

    protected void setDateInternal(int y, int m, int d) {
        this.calendar.set(Calendar.YEAR, y);
        this.calendar.set(Calendar.MONTH, m);
        this.calendar.set(Calendar.DAY_OF_MONTH, Math.min(this.calendar.getMaximum(Calendar.DAY_OF_MONTH), d));
    }

    public void setDateTime(int y, int mo, int d, int h, int mi, int s) {
        this.valueChanging = true;
        this.setDateInternal(y, mo, d);
        this.setTimeInternal(h, mi, s);
        this.rebuildDateSelectionComponent();
        this.updateValues();
        this.valueChanging = false;
    }

    /**
     * 
     * @see java.awt.Component#setLocale(java.util.Locale)
     */
    @Override
    public void setLocale(Locale l) {
        super.setLocale(l);
        this.monthStrings = Arrays.asList(EDateTimeChooser.getMonthStrings(l));
        this.dayStrings = Arrays.asList(EDateTimeChooser.getDayStrings(l));
        this.getMonthLabel().setText(Messages.getString(l, "EDateTimeChooser.month"));
        this.getYearLabel().setText(Messages.getString(l, "EDateTimeChooser.year"));
        this.getTimeLabel().setText(Messages.getString(l, "EDateTimeChooser.time"));
    }

    public void setTime(int h, int m, int s) {
        this.valueChanging = true;
        this.setTimeInternal(h, m, s);
        this.rebuildDateSelectionComponent();
        this.updateValues();
        this.valueChanging = false;
    }

    protected void setTimeInternal(int h, int m, int s) {
        this.calendar.set(Calendar.HOUR_OF_DAY, h);
        this.calendar.set(Calendar.MINUTE, m);
        this.calendar.set(Calendar.SECOND, s);
    }

    protected void updateValues() {
        this.getMonthSpinner().setValue(this.monthStrings.get(this.calendar.get(Calendar.MONTH)));
        this.getYearSpinner().setValue(this.calendar.get(Calendar.YEAR));
        this.getTimeSpinner().setValue(this.calendar.getTime());
    }
}
