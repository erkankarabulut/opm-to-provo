package cgl.cluster;
/*
 * <p>Title: Preference.java</p>
 * <p>Description: Preference Class
 * </p>
 * <p>authors: M Aktas & M Nacar
 */

import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;

public class Preference {

  private boolean _gov = false;
  private boolean _edu = false;
  private boolean _mil = false;
  private boolean _net = false;
  private boolean _com = false;
  private boolean _org = false;

  private boolean _ame = false;
  private boolean _eur = false;
  private boolean _asi = false;

  public Preference(Collection col) {
    Iterator iter = col.iterator();
    while (iter.hasNext()) {
      String token = ( (Object) iter.next()).toString();
      if (token.equals("gov")) {
        set_gov(true);
      }
      if (token.equals("edu")) {
        set_edu(true);
      }
      if (token.equals("mil")) {
        set_mil(true);
      }
      if (token.equals("net")) {
        set_net(true);
      }
      if (token.equals("com")) {
        set_com(true);
      }
      if (token.equals("org")) {
        set_org(true);
      }
      if (token.equals("ame")) {
        set_ame(true);
      }
      if (token.equals("eur")) {
        set_eur(true);
      }
      if (token.equals("asi")) {
        set_asi(true);
      }
    }
  }

  boolean get_gov() {
    return _gov;
  }

  void set_gov(boolean g) {
    _gov = g;
  }

  boolean get_edu() {
    return _edu;
  }

  void set_edu(boolean e) {
    _edu = e;
  }

  boolean get_mil() {
    return _mil;
  }

  void set_mil(boolean m) {
    _mil = m;
  }

  boolean get_net() {
    return _net;
  }

  void set_net(boolean n) {
    _net = n;
  }

  boolean get_com() {
    return _com;
  }

  void set_com(boolean c) {
    _com = c;
  }

  boolean get_org() {
    return _org;
  }

  void set_org(boolean o) {
    _org = o;
  }

  boolean get_ame() {
    return _ame;
  }

  void set_ame(boolean u) {
    _ame = u;
  }

  boolean get_eur() {
    return _eur;
  }

  void set_eur(boolean e) {
    _eur = e;
  }

  boolean get_asi() {
    return _asi;
  }

  void set_asi(boolean a) {
    _asi = a;
  }

}