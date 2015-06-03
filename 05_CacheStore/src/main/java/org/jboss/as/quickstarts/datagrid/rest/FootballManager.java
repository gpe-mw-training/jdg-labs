/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.datagrid.rest;

import java.io.Console;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * @author Martin Gencur
 */
public class FootballManager {

    private static final String JDG_HOST = "jdg.host";
    // REST specific properties
    public static final String HTTP_PORT = "jdg.http.port";
    public static final String REST_CONTEXT_PATH = "jdg.rest.context.path";
    public static final String CACHE_NAME = "jdg.cache.name";

    private static final String msgTeamMissing = "The specified team \"%s\" does not exist, choose next operation\n";
    private static final String msgEnterTeamName = "Enter team name: ";
    private static final String initialPrompt = "Choose action:\n" + "============= \n" + "at  -  add a team\n"
            + "ap  -  add a player to a team\n" + "rt  -  remove a team\n" + "rp  -  remove a player from a team\n"
            + "l   -  list all teams\n"
            + "p   -  print all teams and players\n" + "q   -  quit\n";
    private static final String teamsKey = "teams";
    private String cacheName = "teams";

    private Console con;
    private RESTCache<String, Object> localMap;

    public FootballManager(Console con) {
        this.con = con;
        String contextPath = System.getProperty(REST_CONTEXT_PATH);
        if (contextPath.length() > 0 && !contextPath.startsWith("/")) {
            contextPath = "/" + contextPath;
        }
        String cacheNameString = System.getProperty(CACHE_NAME);
        if ((cacheNameString != null) && (cacheNameString.length() > 0)) {
            cacheName = cacheNameString;
        }
        
        String cacheUrl = "http://" + System.getProperty(JDG_HOST) + ":" + System.getProperty(HTTP_PORT) + contextPath + "/";
        System.out.println("FootballManager() cacheUrl = "+cacheUrl+cacheName+"\n");
        localMap = new RESTCache<String, Object>(cacheName, cacheUrl);
        List<String> teams = (List<String>) localMap.get(teamsKey);
        if (teams == null) {
            teams = new ArrayList<String>();
            Team t = new Team("Barcelona");
            t.addPlayer("Messi");
            t.addPlayer("Pedro");
            t.addPlayer("Puyol");
            localMap.put(t.getName(), t);
            teams.add(t.getName());
            localMap.put(teamsKey, teams);
        }
    }

    public void addTeam() {
        String teamName = con.readLine(msgEnterTeamName);
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) localMap.get(encode(teamsKey));
        if (teams == null) {
            teams = new ArrayList<String>();
        }
        Team t = new Team(teamName);
        localMap.put(encode(teamName), t);
        teams.add(teamName);
        // maintain a list of teams under common key
        localMap.put(teamsKey, teams);
    }

    public void addPlayers() {
        String teamName = con.readLine(msgEnterTeamName);
        String playerName = null;
        Team t = (Team) localMap.get(encode(teamName));
        if (t != null) {
            while (!(playerName = con.readLine("Enter player's name (to stop adding, type \"q\"): ")).equals("q")) {
                t.addPlayer(playerName);
            }
            localMap.put(encode(teamName), t);
        } else {
            con.printf(msgTeamMissing, teamName);
        }
    }

    public void removePlayer() {
        String playerName = con.readLine("Enter player's name: ");
        String teamName = con.readLine("Enter player's team: ");
        Team t = (Team) localMap.get(encode(teamName));
        if (t != null) {
            t.removePlayer(playerName);
            localMap.put(encode(teamName), t);
        } else {
            con.printf(msgTeamMissing, teamName);
        }
    }

    public void removeTeam() {
        String teamName = con.readLine(msgEnterTeamName);

        // Remove entry from JDG server grid
        Team t = (Team) localMap.get(encode(teamName));
        if (t != null) {
            localMap.remove(encode(teamName));
        } else {
            con.printf(msgTeamMissing, teamName);
        }

        // Remove key from List of teams 
        List<String> teams = (List<String>) localMap.get(teamsKey);
        if(teams != null) {
            teams.remove(teamName);
            localMap.put(teamsKey, teams);
        }
    }

    public void listTeams() {
        List<String> teams = (List<String>) localMap.get(teamsKey);
        if (teams != null) {
            int x=1;
            for (String teamName : teams) {
                con.printf(x +") "+teamName+"\n");
                x++;
            }
        }else {
            System.out.println("listTeams() no teams found in cache: "+teamsKey);
        }
    }

    public void printTeams() {
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) localMap.get(teamsKey);
        System.out.println("printTeams number of cached entries = "+teams.size());
        if (teams != null) {
            for (String teamName : teams) {
                String encodedTeam = encode(teamName);
                Object cachedTeam = localMap.get(encodedTeam);
                System.out.println("encodedTeam = "+encodedTeam+" : cachedTeam = "+cachedTeam);
                if(cachedTeam != null) {
                    con.printf(cachedTeam.toString());
                }else {
                    con.printf("printTeams() **** following team is referenced in the Teams List but appears to no longer exist as an entry in the grid: "+encodedTeam+"\nWas this team evicted from the grid?");
                }
            }
        }else {
           System.out.println("printTeams() no teams found in cache: "+teamsKey); 
        }
    }

    public static void main(String[] args) {
        Console con = System.console();
        FootballManager manager = new FootballManager(System.console());
        con.printf(initialPrompt);

        while (true) {
            String action = con.readLine(">");
            if ("at".equals(action)) {
                manager.addTeam();
            } else if ("ap".equals(action)) {
                manager.addPlayers();
            } else if ("rt".equals(action)) {
                manager.removeTeam();
            } else if ("rp".equals(action)) {
                manager.removePlayer();
            } else if ("l".equals(action)) {
                manager.listTeams();
            } else if ("p".equals(action)) {
                manager.printTeams();
            } else if ("q".equals(action)) {
                break;
            }
        }
    }

    public static String encode(String key) {
        try {
            return URLEncoder.encode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
