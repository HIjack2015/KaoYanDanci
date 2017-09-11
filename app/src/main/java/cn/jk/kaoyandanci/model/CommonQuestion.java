package cn.jk.kaoyandanci.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/30.
 */

public class CommonQuestion implements Serializable {
    private String question;
    private String solution;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSolution() {
        return solution;
    }

    public void setSolution(String solution) {
        this.solution = solution;
    }
}
