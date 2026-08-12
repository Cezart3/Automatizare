package model.glass;

public class Glass {
    private int id;
    private String tipSticla;
    private String grosimeMm;
    private Double simplaDebitata;
    private Double securizataCalita;
    private Double manoperaSlefuire;
    private Double manoperaGaurire4_20;
    private Double manoperaGaurire21_30;
    private Double manoperaGaurire31_60_cnc;
    private Double adaosFormaProc;
    private Double adaosSablonProc;
    private Double manoperaDecupeFeron;



    public Glass(int id, String tipSticla, String grosimeMm,
                 Double simplaDebitata,
                 Double securizataCalita,
                 Double manoperaSlefuire,
                 Double manoperaGaurire4_20,
                 Double manoperaGaurire21_30,
                 Double manoperaGaurire31_60_cnc,
                 Double adaosFormaProc,
                 Double adaosSablonProc,
                 Double manoperaDecupeFeron) {
        this.id = id;
        this.tipSticla = tipSticla;
        this.grosimeMm = grosimeMm;
        this.simplaDebitata = simplaDebitata;
        this.securizataCalita = securizataCalita;
        this.manoperaSlefuire = manoperaSlefuire;
        this.manoperaGaurire4_20 = manoperaGaurire4_20;
        this.manoperaGaurire21_30 = manoperaGaurire21_30;
        this.manoperaGaurire31_60_cnc = manoperaGaurire31_60_cnc;
        this.adaosFormaProc = adaosFormaProc;
        this.adaosSablonProc = adaosSablonProc;
        this.manoperaDecupeFeron = manoperaDecupeFeron;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTipSticla(String tipSticla) {
        this.tipSticla = tipSticla;
    }

    public void setGrosimeMm(String grosimeMm) {
        this.grosimeMm = grosimeMm;
    }

    public void setSimplaDebitata(Double simplaDebitata) {
        this.simplaDebitata = simplaDebitata;
    }

    public void setSecurizataCalita(Double securizataCalita) {
        this.securizataCalita = securizataCalita;
    }

    public void setManoperaSlefuire(Double manoperaSlefuire) {
        this.manoperaSlefuire = manoperaSlefuire;
    }

    public void setManoperaGaurire4_20(Double manoperaGaurire4_20) {
        this.manoperaGaurire4_20 = manoperaGaurire4_20;
    }

    public void setManoperaGaurire21_30(Double manoperaGaurire21_30) {
        this.manoperaGaurire21_30 = manoperaGaurire21_30;
    }

    public void setManoperaGaurire31_60_cnc(Double manoperaGaurire31_60_cnc) {
        this.manoperaGaurire31_60_cnc = manoperaGaurire31_60_cnc;
    }

    public void setAdaosFormaProc(Double adaosFormaProc) {
        this.adaosFormaProc = adaosFormaProc;
    }

    public void setAdaosSablonProc(Double adaosSablonProc) {
        this.adaosSablonProc = adaosSablonProc;
    }

    public void setManoperaDecupeFeron(Double manoperaDecupeFeron) {
        this.manoperaDecupeFeron = manoperaDecupeFeron;
    }


    public int getId() { return id; }
    public String getTipSticla() { return tipSticla; }
    public String getGrosimeMm() { return grosimeMm; }
    public Double getSimplaDebitata() { return simplaDebitata; }
    public Double getSecurizataCalita() { return securizataCalita; }
    public Double getManoperaSlefuire() { return manoperaSlefuire; }
    public Double getManoperaGaurire4_20() { return manoperaGaurire4_20; }
    public Double getManoperaGaurire21_30() { return manoperaGaurire21_30; }

    public Double getManoperaGaurire31_60_cnc() {
        return manoperaGaurire31_60_cnc;
    }
    public Double getAdaosFormaProc() { return adaosFormaProc; }
    public Double getAdaosSablonProc() { return adaosSablonProc; }
    public Double getManoperaDecupeFeron() { return manoperaDecupeFeron; }
}
