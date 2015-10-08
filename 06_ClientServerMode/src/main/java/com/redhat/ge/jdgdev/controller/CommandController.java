package com.redhat.ge.jdgdev.controller;

import javax.validation.Valid;
import com.redhat.ge.jdgdev.form.Player;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import com.redhat.ge.jdgdev.FootballManager;
import java.util.List;

@Controller
public class CommandController {
	
	private FootballManager fb;

	@RequestMapping(value = "/printteams", method = RequestMethod.GET)
	public ModelAndView showPrintTeamsForm() {
		return new ModelAndView("printteamsForm", "player", new Player());
	}
	
	@RequestMapping(value = "/addteam", method = RequestMethod.GET)
	public ModelAndView showAddTeamForm() {
		return new ModelAndView("addteamForm", "player", new Player());
	}
	
	@RequestMapping(value = "/removeteam", method = RequestMethod.GET)
	public ModelAndView showRemoveTeamForm() {
		return new ModelAndView("removeteamForm", "player", new Player());
	}
	
	@RequestMapping(value = "/addplayer", method = RequestMethod.GET)
	public ModelAndView showAddPlayerForm() {
		return new ModelAndView("addplayerForm", "player", new Player());
	}	
	
	@RequestMapping(value = "/removeplayer", method = RequestMethod.GET)
	public ModelAndView showRemovePlayerForm() {
		return new ModelAndView("removeplayerForm", "player", new Player());
	}	

	@RequestMapping(value = "/printteamsExec", method = RequestMethod.POST)
	public String submitPrintTeams(@Valid @ModelAttribute("player") final Player player, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		fb = new FootballManager();
		model.addAttribute("output", fb.printTeams());
		return "printteamsResult";
	}

	@RequestMapping(value = "/addteamExec", method = RequestMethod.POST)
	public String submitAddTeam(@Valid @ModelAttribute("player") final Player player, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		fb = new FootballManager();
		fb.addTeam(player.getTeam());
		model.addAttribute("team", player.getTeam());
		return "addteamResult";
	}	

	@RequestMapping(value = "/removeteamExec", method = RequestMethod.POST)
	public String submitRemoveTeam(@Valid @ModelAttribute("player") final Player player, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		fb = new FootballManager();
		fb.removeTeam(player.getTeam());
		model.addAttribute("team", player.getTeam());
		return "removeteamResult";
	}	

	@RequestMapping(value = "/addplayerExec", method = RequestMethod.POST)
	public String submitAddPlayer(@Valid @ModelAttribute("player") final Player player, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		fb = new FootballManager();
		fb.addPlayer(player.getName(),player.getTeam());
		model.addAttribute("name", player.getName());
		model.addAttribute("team", player.getTeam());
		return "addplayerResult";
	}	

	@RequestMapping(value = "/removeplayerExec", method = RequestMethod.POST)
	public String submitRemovePlayer(@Valid @ModelAttribute("player") final Player player, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return "error";
		}
		fb = new FootballManager();
		fb.removePlayer(player.getName(),player.getTeam());
		model.addAttribute("name", player.getName());
		model.addAttribute("team", player.getTeam());
		return "removeplayerResult";
	}	
	
}
