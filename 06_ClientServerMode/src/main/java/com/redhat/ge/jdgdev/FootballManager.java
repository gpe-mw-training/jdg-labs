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
package com.redhat.ge.jdgdev;

import java.util.ArrayList;
import java.io.IOException;
import java.util.List;

import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;

import java.util.Properties;


public class FootballManager {

    private static final String JDG_HOST = "jdg.host";
    private static final String HOTROD_PORT = "jdg.hotrod.port";
    private static final String PROPERTIES_FILE = "jdg.properties";
    private static final String msgTeamMissing = "The specified team \"%s\" does not exist, choose next operation\n";
    private static final String teamsKey = "teams";
    private RemoteCacheManager cacheManager;
    private RemoteCache<String, Object> cache;

    public FootballManager() {
        ConfigurationBuilder builder = new ConfigurationBuilder();
        System.out.println("FootballManager App host = "+jdgProperty(JDG_HOST)+" || port = "+jdgProperty(HOTROD_PORT));
        builder.addServer()
              .host(jdgProperty(JDG_HOST))
              .port(Integer.parseInt(jdgProperty(HOTROD_PORT)));
        cacheManager = new RemoteCacheManager(builder.build());
        cache = cacheManager.getCache("teams");
        if(!cache.containsKey(teamsKey)) {
            List<String> teams = new ArrayList<String>();
            Team t = new Team("Barcelona");
            t.addPlayer("Messi");
            t.addPlayer("Pedro");
            t.addPlayer("Puyol");
            cache.put(t.getName(), t);
            teams.add(t.getName());
            cache.put(teamsKey, teams);
        }
    }

    public void addTeam(String name) {
        String teamName = name;
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(teamsKey);
        if (teams == null) {
            teams = new ArrayList<String>();
        }
        Team t = new Team(teamName);
        cache.put(teamName, t);
        teams.add(teamName);
        // maintain a list of teams under common key
        cache.put(teamsKey, teams);
    }

    public void addPlayer(String playerName, String teamName) 
    {
        Team t = (Team) cache.get(teamName);
        if (t != null) {
                t.addPlayer(playerName);
            cache.put(teamName, t);
        } else {
            System.out.printf(msgTeamMissing, teamName);       
    }
    }
    
    public void removePlayer(String playerName, String teamName) {
        Team t = (Team) cache.get(teamName);
        if (t != null) {
            t.removePlayer(playerName);
            cache.put(teamName, t);
        } else {
        	System.out.printf(msgTeamMissing, teamName);
        }
    }

    public void removeTeam(String teamName) {
        Team t = (Team) cache.get(teamName);
        if (t != null) {
            cache.remove(teamName);
            @SuppressWarnings("unchecked")
            List<String> teams = (List<String>) cache.get(teamsKey);
            if (teams != null) {
                teams.remove(teamName);
            }
            cache.put(teamsKey, teams);
        } else {
        	System.out.printf(msgTeamMissing, teamName);
        }
    }

    public String printTeams() {
        @SuppressWarnings("unchecked")
        List<String> teams = (List<String>) cache.get(teamsKey);
        StringBuilder output = new StringBuilder();
        if (teams != null) {
            for (String teamName : teams) {
            	String a = cache.get(teamName).toString();
                output.append(a);
                System.out.println(a);
            }
        }
        return output.toString();
    }

    public void stop() {
        cacheManager.stop();
    }

//    public static void main(String[] args) {
//        Console con = System.console();
//        FootballManager manager = new FootballManager();
//        con.printf(initialPrompt);
//  while (true) { }
//  String action = con.readLine(">");
//                break;
    //    public void controller(String command, String playerName, String teamName) {
    //      if ("at".equals(command)) {
    //          addTeam(teamName);
    //        } else if ("ap".equals(command)) {
    //           addPlayer(playerName,teamName);
    //       } else if ("rt".equals(command)) {
    //           removeTeam(teamName);
    //       } else if ("rp".equals(command)) {
    //           removePlayer(playerName, teamName);
    //       } else if ("p".equals(command)) {
    //           printTeams();
    //       } 
    //}

    public String jdgProperty(String name) {
    	Properties props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE));
 
        } catch (IOException ioe) {
            System.out.println(ioe);
            throw new RuntimeException(ioe);
        }
        return props.getProperty(name);
    }

}