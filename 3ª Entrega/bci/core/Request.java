package bci.core;

import java.io.*;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = 202501101053L;

    private final User _user;
    private final Work _work;
    private final int _startDay;
    private final int _deadline;
    private Integer _returnDay;

    public static final int DEFAULT_DAILY_FINE = 5;

    public Request(User user, Work work, int currentDay, int allowedDays) {
    _user = user;
    _work = work;
    _startDay = currentDay;
    _deadline = currentDay + allowedDays;
    _returnDay = null;
    }

    public User getUser(){ 
        return _user; 
    }

    public Work getWork(){ 
        return _work; 
    }

    public int getStartDay(){ 
        return _startDay; 
    }

    public int getDeadline(){ 
        return _deadline; 
    }

    public boolean isClosed(){ 
        return _returnDay != null;
    }


    public Integer getReturnDay(){ 
        return _returnDay; 
    }


    public void close(int currentDay){
        if (_returnDay == null){
            _returnDay = currentDay;
        }
    }

    public int daysLate(int asOfDay) {
        int effectiveDay = isClosed() ? _returnDay : asOfDay;
        int delta = effectiveDay - _deadline;
        return Math.max(0, delta);
    }

    public int computeFine(int asOfDay, Integer dailyFine) {
        int fine = (dailyFine != null ? dailyFine : DEFAULT_DAILY_FINE);
        return daysLate(asOfDay) * fine;
    }
}
