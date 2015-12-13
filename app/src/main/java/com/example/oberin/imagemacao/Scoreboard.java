package com.example.oberin.imagemacao;

import android.os.Parcel;
import android.os.Parcelable;

public class Scoreboard implements Parcelable {
    String userHome;
    Integer pointsHome;
    String userAway;
    Integer pointsAway;

    public Scoreboard(String userHome, Integer pointsHome, String userAway, Integer pointsAway) {
        this.userHome = userHome;
        this.pointsHome = pointsHome;
        this.userAway = userHome;
        this.pointsAway = pointsAway;
    }

    public String getUserHome() {
        return userHome;
    }

    public Integer getPointsHome() {
        return pointsHome;
    }

    public String getUserAway() {
        return userAway;
    }

    public Integer getPointsAway() {
        return pointsAway;
    }

    public void setUserHome(String userHome) {
        this.userHome = userHome;
    }

    public void setPointsHome(Integer pointsHome) {
        this.pointsHome = pointsHome;
    }

    public void setUserAway(String userAway) {
        this.userAway = userAway;
    }

    public void setPointsAway(Integer pointsAway) {
        this.pointsAway = pointsAway;
    }

    public String getMatchResult() {
        String result;

        if (this.getPointsHome() > this.getPointsAway()){
            result = "Professor ganhou!";
        }
        else if (this.getPointsHome() < this.getPointsAway()) {
            result = "Aluno ganhou!";
        }
        else {
            result = "Empatou!";
        }

        return result;
    }

    //parcel part
    public Scoreboard(Parcel in) {
        String[] data = new String[4];

        in.readStringArray(data);
        this.userHome = data[0];
        this.pointsHome = Integer.parseInt(data[1]);
        this.userAway = data[2];
        this.pointsAway = Integer.parseInt(data[3]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{this.userHome,
                String.valueOf(this.pointsHome),
                this.userAway,
                String.valueOf(this.pointsAway)
        });
    }

    public static final Parcelable.Creator<Scoreboard> CREATOR = new Parcelable.Creator<Scoreboard>() {

        @Override
        public Scoreboard createFromParcel(Parcel source) {
            return new Scoreboard(source);  //using parcelable constructor
        }

        @Override
        public Scoreboard[] newArray(int size) {
            return new Scoreboard[size];
        }
    };
}