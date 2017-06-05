package com.example.miya_.myapplication;

/**
 * ExerciseItem
 * Description:
 *      prepare data for the view of exerciseItem element
 *          including name, sets, reps and state
 */
public class ExerciseItem {

    public ExerciseItem() {
    }

    public int ID;
    public String Name;
    public int Set;
    public int Rept;
    public boolean box;


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name1) {
        this.Name = Name1;
    }

    public int getSet() {
        return Set;
    }

    public void setSet(int Set1) {
        this.Set = Set1;
    }

    public int getRept() {
        return Rept;
    }

    public void setRept(int Rept1) {
        this.Rept = Rept1;
    }
    public boolean getBox()
    {
        return box;
    }

    public void setBox(boolean isChecked)
    {
        this.box = isChecked;
    }

}
