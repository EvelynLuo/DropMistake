package bupt.dropmistake.tool;

public class ProblemData {
    private String _titleId;
    private String _dateId;
    private int _diffId;
    private String _kpId;
    private String _imgId;
    private String _ansId;

    public ProblemData(String _dateId, int _diffId, String _kpId, String _imgId, String _ansId) {
        this._dateId = _dateId;
        this._diffId = _diffId;
        this._kpId = _kpId;
        this._imgId = _imgId;
        this._ansId = _ansId;
    }

    public String get_titleId() {
        return _titleId;
    }

    public void set_titleId(String _titleId) {
        this._titleId = _titleId;
    }

    public String get_dateId() {
        return _dateId;
    }

    public void set_dateId(String _dateId) {
        this._dateId = _dateId;
    }

    public int get_diffId() {
        return _diffId;
    }

    public void set_diffId(int _diffId) {
        this._diffId = _diffId;
    }

    public String get_kpId() {
        return _kpId;
    }

    public void set_kpId(String _kpId) {
        this._kpId = _kpId;
    }

    public String get_imgId() {
        return _imgId;
    }

    public void set_imgId(String _imgId) {
        this._imgId = _imgId;
    }

    public String get_ansId() {
        return _ansId;
    }

    public void set_ansId(String _ansId) {
        this._ansId = _ansId;
    }
}
