package controllers.employees;

import models.Employee;

public class FollowChk {
    private Employee emp;
    private boolean chk = false;

    public Employee getEmp() {
        return emp;
    }
    public void setEmp(Employee emp) {
        this.emp = emp;
    }
    public boolean isChk() {
        return chk;
    }
    public void setChk(boolean chk) {
        this.chk = chk;
    }
}

