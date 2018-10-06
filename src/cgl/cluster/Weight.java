package cgl.cluster;
/*
 * <p>Title: Weight.java</p>
 * <p>Description: Weight Correlation Class
 * </p>
 * <p>authors: M Aktas & M Nacar
 */

import java.util.StringTokenizer;
import java.util.*;

public class Weight {

  static final int weights_size = 27;
  static final int _gov = 0;
  static final int _edu = 1;
  static final int _mil = 2;
  static final int _net = 3;
  static final int _com = 4;
  static final int _org = 5;
  static final int _ame = 6;
  static final int _eur = 7;
  static final int _asi = 8;
  static final int _gov_ame = 9;
  static final int _edu_ame = 10;
  static final int _mil_ame = 11;
  static final int _net_ame = 12;
  static final int _com_ame = 13;
  static final int _org_ame = 14;
  static final int _gov_eur = 15;
  static final int _edu_eur = 16;
  static final int _mil_eur = 17;
  static final int _net_eur = 18;
  static final int _com_eur = 19;
  static final int _org_eur = 20;
  static final int _gov_asi = 21;
  static final int _edu_asi = 22;
  static final int _mil_asi = 23;
  static final int _net_asi = 24;
  static final int _com_asi = 25;
  static final int _org_asi = 26;

  static private double[] weights;


  private Weight(double [] array) {
    for (int i = 0; i < array.length; i++) {
      weights[i] = array[i];
    }
  }

  //".gov.edu.net.com.mil.org.america.europe.asia"
 static public Weight instance(Vector vector) {
   weights = new double[weights_size];
   for (int i = 0; i < weights.length; i++) {
     weights[i] = 1.0;
   }
   Preference p = new Preference(vector);

   Iterator iter = vector.iterator();
   //System.out.print("combination = ");
   //while (iter.hasNext()) {
   //  System.out.print(iter.next().toString() + " ");
   //}
   //System.out.println();

   double max = 1.0;
   if (p.get_gov()) {
     weights[_gov] = 2.0;
   }
   if (p.get_edu()) {
     weights[_edu] = 2.0;
   }
   if (p.get_mil()) {weights[_mil] = 2.0;}
   if (p.get_net()) {weights[_net] = 2.0;}
   if (p.get_com()) {weights[_com] = 2.0;}
   if (p.get_org()) {weights[_org] = 2.0;}
   if (p.get_ame()) {weights[_ame] = 2.0;}
   if (p.get_eur()) {weights[_eur] = 2.0;}
   if (p.get_asi()) {weights[_asi] = 2.0;}
   if (p.get_gov() && p.get_ame()) {weights[_gov_ame] = 4.0;}
   if (p.get_edu() && p.get_ame()) {weights[_edu_ame] = 4.0;}
   if (p.get_mil() && p.get_ame()) {weights[_mil_ame] = 4.0;}
   if (p.get_net() && p.get_ame()) {weights[_net_ame] = 4.0;}
   if (p.get_com() && p.get_ame()) {weights[_com_ame] = 4.0;}
   if (p.get_org() && p.get_ame()) {weights[_org_ame] = 4.0;}
   if (p.get_gov() && p.get_eur()) {weights[_gov_eur] = 4.0;}
   if (p.get_edu() && p.get_eur()) {weights[_edu_eur] = 4.0;}
   if (p.get_mil() && p.get_eur()) {weights[_mil_eur] = 4.0;}
   if (p.get_net() && p.get_eur()) {weights[_net_eur] = 4.0;}
   if (p.get_com() && p.get_eur()) {weights[_com_eur] = 4.0;}
   if (p.get_org() && p.get_eur()) {weights[_org_eur] = 4.0;}
   if (p.get_gov() && p.get_asi()) {weights[_gov_asi] = 4.0;}
   if (p.get_edu() && p.get_asi()) {weights[_edu_asi] = 4.0;}
   if (p.get_mil() && p.get_asi()) {weights[_mil_asi] = 4.0;}
   if (p.get_net() && p.get_asi()) {weights[_net_asi] = 4.0;}
   if (p.get_com() && p.get_asi()) {weights[_com_asi] = 4.0;}
   if (p.get_org() && p.get_asi()) {weights[_org_asi] = 4.0;}

   for (int i = 0; i < weights.length; i++) {
     if (max < weights[i])
       max = weights[i];
   }

   //System.out.print("weights = ");
   for (int j = 0; j < weights.length; j++) {
     weights[j] = weights[j] / max;
     //System.out.print(weights[j] + "  ");
   }
   //System.out.println();

   return new Weight(weights);
 }

 public double getWeight(String token) {
   if (token.equals("gov")) {return get_gov();}
   else if (token.equals("edu")) {return get_edu();}
   else if (token.equals("mil")) {return get_mil();}
   else if (token.equals("net")) {return get_net();}
   else if (token.equals("com")) {return get_com();}
   else if (token.equals("org")) {return get_org();}
   else if (token.equals("ame")) {return get_ame();}
   else if (token.equals("eur")) {return get_eur();}
   else if (token.equals("asi")) {return get_asi();}
   else if (token.equals("gov_ame")) {return get_gov_ame();}
   else if (token.equals("edu_ame")) {return get_edu_ame();}
   else if (token.equals("mil_ame")) {return get_mil_ame();}
   else if (token.equals("net_ame")) {return get_net_ame();}
   else if (token.equals("com_ame")) {return get_com_ame();}
   else if (token.equals("org_ame")) {return get_org_ame();}

   else if (token.equals("gov_eur")) {return get_gov_eur();}
   else if (token.equals("edu_eur")) {return get_edu_eur();}
   else if (token.equals("mil_eur")) {return get_mil_eur();}
   else if (token.equals("net_eur")) {return get_net_eur();}
   else if (token.equals("com_eur")) {return get_com_eur();}
   else if (token.equals("org_eur")) {return get_org_eur();}

   else if (token.equals("gov_asi")) {return get_gov_asi();}
   else if (token.equals("edu_asi")) {return get_edu_asi();}
   else if (token.equals("mil_asi")) {return get_mil_asi();}
   else if (token.equals("net_asi")) {return get_net_asi();}
   else if (token.equals("com_asi")) {return get_com_asi();}
   else if (token.equals("org_asi")) {return get_org_asi();}
   else return -1.0;
 }

 public double get_gov() {return weights[_gov];}
 public void set_gov(double g) {weights[_gov] = g;}
 public double get_edu() {return weights[_edu];}
 public void set_edu(double e) {weights[_edu] = e;}
 public double get_mil() {return weights[_mil];}
 public void set_mil(double m) {weights[_mil] = m;}
 public double get_net() {return weights[_net];}
 public void set_net(double n) {weights[_net] = n;}
 public double get_com() {return weights[_com];}
 public void set_com(double c) {weights[_com] = c;}
 public double get_org() {return weights[_org];}
 public void set_org(double o) {weights[_org] = o;}
 public double get_ame() {return weights[_ame];}
 public void set_ame(double u) {weights[_ame] = u;}
 public double get_eur() {return weights[_eur];}
 public void set_eur(double e) {weights[_eur] = e;}
 public double get_asi() {return weights[_asi];}
 public void set_asi(double a) {weights[_asi] = a;}
 public double get_gov_ame() {return weights[_gov_ame];}
 public void set_gov_ame(double g) {weights[_gov_ame] = g;}
 public double get_edu_ame() {return weights[_edu_ame];}
 public void set_edu_ame(double e) {weights[_edu_ame] = e;}
 public double get_mil_ame() {return weights[_mil_ame];}
 public void set_mil_ame(double m) {weights[_mil_ame] = m;}
 public double get_net_ame() {return weights[_net_ame];}
 public void set_net_ame(double n) {weights[_net_ame] = n;}
 public double get_com_ame() {return weights[_com_ame];}
 public void set_com_ame(double c) {weights[_com_ame] = c;}
 public double get_org_ame() {return weights[_org_ame];}
 public void set_org_ame(double o) {weights[_org_ame] = o;}
 public double get_gov_eur() {return weights[_gov_eur];}
 public void set_gov_eur(double g) {weights[_gov_eur] = g;}
 public double get_edu_eur() {return weights[_edu_eur];}
 public void set_edu_eur(double e) {weights[_edu_eur] = e;}
 public double get_mil_eur() {return weights[_mil_eur];}
 public void set_mil_eur(double m) {weights[_mil_eur] = m;}
 public double get_net_eur() {return weights[_net_eur];}
 public void set_net_eur(double n) {weights[_net_eur] = n;}
 public double get_com_eur() {return weights[_com_eur];}
 public void set_com_eur(double c) {weights[_com_eur] = c;}
 public double get_org_eur() {return weights[_org_eur];}
 public void set_org_eur(double o) {weights[_org_eur] = o;}
 public double get_gov_asi() {return weights[_gov_asi];}
 public void set_gov_asi(double g) {weights[_gov_asi] = g;}
 public double get_edu_asi() {return weights[_edu_asi];}
 public void set_edu_asi(double e) {weights[_edu_asi] = e;}
 public double get_mil_asi() {return weights[_mil_asi];}
 public void set_mil_asi(double m) {weights[_mil_asi] = m;}
 public double get_net_asi() {return weights[_net_asi];}
 public void set_net_asi(double n) {weights[_net_asi] = n;}
 public double get_com_asi() {return weights[_com_asi];}
 public void set_com_asi(double c) {weights[_com_asi] = c;}
 public double get_org_asi() {return weights[_org_asi];}
 public void set_org_asi(double o) {weights[_org_asi] = o;}


}