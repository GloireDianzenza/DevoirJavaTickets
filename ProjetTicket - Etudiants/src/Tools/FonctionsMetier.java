/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tools;

import Entity.Etat;
import Entity.Ticket;
import Entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jbuffeteau
 */
public class FonctionsMetier 
{
   private PreparedStatement ps;
   private ResultSet rs;
   private Connection cnx;
   
   public FonctionsMetier()
   {
       cnx = ConnexionBDD.getCnx();
   }
   
   public User chercherUser(String login,String mdp)
   {
       User testUser = null;
       try {
           ps = cnx.prepareStatement("SELECT idUser,nomUser,prenomUser,statutUser FROM users WHERE loginUser = ? AND pwdUser = ?");
           ps.setString(1, login);
           ps.setString(2, mdp);
           
           rs = ps.executeQuery();
           
           while(rs.next())
           {
               testUser = new User(rs.getInt("idUser"), rs.getString("nomUser"), rs.getString("prenomUser"), rs.getString("statutUser"));
           }
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       return testUser;
   }
   
   public ArrayList<Ticket> getTicketUser(int id)
   {
       ArrayList<Ticket> tickets = new ArrayList<>();
       try
       {
           ps = cnx.prepareStatement("SELECT idTicket,nomTicket,dateTicket,etats.nomEtat FROM tickets JOIN etats ON tickets.numEtat = etats.idEtat WHERE numUser = '"+id+"'");
           rs = ps.executeQuery();
           while(rs.next())
           {
               Ticket tic = new Ticket(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
               tickets.add(tic);
           }
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       return tickets;
   }
   
   public ArrayList<User> getUsers()
   {
       ArrayList<User> users = new ArrayList<>();
       try
       {
           ps = cnx.prepareStatement("SELECT idUser,nomUser,prenomUser,statutUser FROM users");
           rs = ps.executeQuery();
           while(rs.next())
           {
               User u = new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4));
               users.add(u);
           }
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       return users;
   }
   
   public int getNextTicket()
   {
       int nextTicket = 0;
       ArrayList<Integer> tickets;
       tickets = new ArrayList<>();
       try
       {
           ps = cnx.prepareStatement("SELECT idTicket from tickets");
           rs = ps.executeQuery();
           while(rs.next())
           {
               tickets.add(rs.getInt(1));
               nextTicket = tickets.size();
           }
           nextTicket += 1;
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       return nextTicket;
   }
   
   public ArrayList<Etat> getEtats()
   {
       ArrayList<Etat> lesEtats = new ArrayList<>();
       try
       {
           ps = cnx.prepareStatement("SELECT idEtat,nomEtat FROM etats");
           rs = ps.executeQuery();
           while(rs.next())
           {
               Etat e = new Etat(rs.getInt(1), rs.getString(2));
               lesEtats.add(e);
           }
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       return lesEtats;
   }
   
   public void addEtat(String etat,int idTicket)
   {
       int idEtat = 0;
       try
       {
           ps = cnx.prepareStatement("SELECT idEtat FROM etats WHERE nomEtat = '"+etat+"'");
           rs = ps.executeQuery();
           if(rs.next())
           {
               idEtat = rs.getInt("idEtat");
           }
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       try
       {
           ps = cnx.prepareStatement("UPDATE tickets SET numEtat = "+idEtat+" WHERE idTicket = "+idTicket);
           ps.executeUpdate();
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
   
   public int getIdEtat(String nomEtat)
   {
       int idEtat = 0;
       try
       {
           ps = cnx.prepareStatement("SELECT idEtat FROM etats WHERE nomEtat = '"+nomEtat+"'");
           rs = ps.executeQuery();
           if(rs.next())
           {
               idEtat = rs.getInt(1);
           }
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
       return idEtat;
   }
   
   public void AjouterTicket(int idTicket,String nomTicket,String dateTicket,int idUser,int idEtat)
   {
       try
       {
           ps = cnx.prepareStatement("INSERT INTO tickets VALUES (?,?,?,?,?)");
           ps.setInt(1, idTicket);
           ps.setString(2, nomTicket);
           ps.setString(3, dateTicket);
           ps.setInt(4, idUser);
           ps.setInt(5, idEtat);
           ps.executeUpdate();
       } catch (SQLException ex) {
            Logger.getLogger(FonctionsMetier.class.getName()).log(Level.SEVERE, null, ex);
        }
   }
}
