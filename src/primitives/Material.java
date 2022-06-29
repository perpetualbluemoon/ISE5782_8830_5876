package primitives;

/***
 * this class describes the material of a geometry
 */
public class Material {
    /***
     * for local effects
     */
    private Double3 _kD = Double3.ZERO;
    /***
     * for local effects
     */
    private Double3 _kS = Double3.ZERO;
    /***
     * for local effects
     */
    public int nShininess = 0;

    /***
     * transparency
     */
    public Double3 kT = new Double3 (0.0);
    /***
     * reflective - mirror
     */
    public Double3 kR = new Double3(0.0);


    /***
     * setters in similar form to builder pattern - they return the current object
     * @param kD kd
     * @return object for builder-like use
     */
    public Material setKd(Double3 kD) {
        _kD = kD;
        return this;
    }

    /***
     * setter
     * @param kS ks
     * @return object for builder-like use
     */
    public Material setKs(Double3 kS) {
        _kS = kS;
        return this;
    }

    /***
     * setter
     * @param kD kd
     * @return object for builder-like use
     */
    public Material setKd(double kD) {
        _kD = new Double3(kD);
        return this;
    }

    /***
     * setter
     * @param kS ks
     * @return object for builder-like use
     */
    public Material setKs(double kS) {
        _kS = new Double3(kS);
        return this;
    }

    /***
     * setter
     * @param nShininess shininess
     * @return object for builder-like use
     */
    public Material setShininess(int nShininess) {
        this.nShininess = nShininess;
        return this;
    }

    /***
     * setter
     * @param kT kt
     * @return object for builder-like use
     */
    public Material setKt(Double3 kT) {
        this.kT = kT;
        return this;
    }

    /***
     * setter
     * @param kR kr
     * @return object for builder-like use
     */
    public Material setKr(Double3 kR) {
        this.kR = kR;
        return this;
    }

    /***
     * getter
     * @return kd
     */
    public Double3 getkD() {
        return _kD;
    }

    /***
     * getter
     * @return ks
     */
    public Double3 getkS() {
        return _kS;
    }

    /***
     * setter
     * @param kt kt
     * @return object for builder-like use
     */
    public Material setkT(double kt) {
        this.kT = new Double3(kt);
        return this;
    }

    /***
     * setter
     * @param kr kr
     * @return object for builder-like use
     */
    public Material setkR(double kr) {
        this.kR = new Double3(kr);
        return this;
    }
}
