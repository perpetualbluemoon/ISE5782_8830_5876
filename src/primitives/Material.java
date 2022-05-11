package primitives;

public class Material {
    //for local effects
    private Double3 _kD = Double3.ZERO;
    private Double3 _kS = Double3.ZERO;
    public int nShininess = 0;


    public Double3 kT = new Double3 (0.0);  //transparency
    public Double3 kR = new Double3(0.0); //reflective - mirror


    /***
     * setters in similar form to builder pattern - they return the current object
     */
    public Material setKd(Double3 kD) {
        _kD = kD;
        return this;
    }

    public Material setKs(Double3 kS) {
        _kS = kS;
        return this;
    }

    public Material setKd(double kD) {
        _kD = new Double3(kD);
        return this;
    }

    public Material setKs(double kS) {
        _kS = new Double3(kS);
        return this;
    }

    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    public Double3 getkD() {
        return _kD;
    }

    public Double3 getkS() {
        return _kS;
    }

    public Material setkT(double kt) {
        this.kT = new Double3(kt);
        return this;
    }
    public Material setkR(double kr) {
        this.kR = new Double3(kr);
        return this;
    }
}
