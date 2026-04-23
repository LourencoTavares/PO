package bci.core;

import java.io.Serial;
import java.io.Serializable;

public class Date implements Serializable{
    @Serial
    private static final long serialVersionUID = 202501101001L;

    private int _currentDate;

    public Date() {
        _currentDate = 1;
    }

    public Date (int currentDate){
        _currentDate = Math.max(1, currentDate);
    }

    int getCurrentDate (){   
        return _currentDate;
    }

    void advanceDays(int nDays) {
        if(nDays > 0){
            _currentDate += nDays;
        }
    }
}