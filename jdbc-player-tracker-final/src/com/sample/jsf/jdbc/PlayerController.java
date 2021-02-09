package com.sample.jsf.jdbc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class PlayerController {

	private List<Player> players;
	private PlayerDbUtil playerDbUtil;
	private Logger logger = Logger.getLogger(getClass().getName());
	
	public PlayerController() throws Exception {
		players = new ArrayList<>();
		
		playerDbUtil = PlayerDbUtil.getInstance();
	}
	
	public List<Player> getPlayers() {
		return players;
	}

	public void loadPlayers() {

		logger.info("Loading players");
		
		players.clear();

		try {
			
			// get all players from database
			players = playerDbUtil.getPlayers();
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading players", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
		}
	}
		
	public String addPlayer(Player thePlayer) {

		logger.info("Adding players: " + thePlayer);

		try {
			
			// add student to the database
			playerDbUtil.addPlayer(thePlayer);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error adding players", exc);
			
			// add error message for JSF page
			addErrorMessage(exc);

			return null;
		}
		
		return "list-players?faces-redirect=true";
	}

	public String loadPlayer(int playerId) {
		
		logger.info("loading player: " + playerId);
		
		try {
			// get student from database
			Player thePlayer = playerDbUtil.getPlayer(playerId);
			
			// put in the request attribute ... so we can use it on the form page
			ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();		

			Map<String, Object> requestMap = externalContext.getRequestMap();
			requestMap.put("player", thePlayer);	
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error loading player id:" + playerId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
				
		return "update-player-form.xhtml";
	}	
	
	public String updatePlayer(Player thePlayer) {

		logger.info("updating player: " + thePlayer);
		
		try {
			
			// update student in the database
			playerDbUtil.updatePlayer(thePlayer);
			
		} catch (Exception exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Error updating player: " + thePlayer, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-players?faces-redirect=true";		
	}
	
	public String deletePlayer(int playerId) {

		logger.info("Deleting player id: " + playerId);
		
		try {

			
			playerDbUtil.deletePlayer(playerId);
			
		} catch (Exception exc) {
			
			logger.log(Level.SEVERE, "Error deleting player id: " + playerId, exc);
			
			// add error message for JSF page
			addErrorMessage(exc);
			
			return null;
		}
		
		return "list-players";	
	}	
	
	private void addErrorMessage(Exception exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
}
