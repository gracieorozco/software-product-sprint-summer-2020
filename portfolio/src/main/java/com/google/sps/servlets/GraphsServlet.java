// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps.servlets;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Scanner;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
* The GraphsServlet retrieves data from the 
* ElectricityByCounty.csv file in a LinkedHashMap
* to be processed by the Google Charts library in 
* JavaScript. 
*/
@WebServlet("/graph-data")
public class GraphsServlet extends HttpServlet {

  LinkedHashMap<String, Double> electricityMap = new LinkedHashMap<>();

  @Override
  public void init() {
    Scanner scanner = new Scanner(getServletContext().getResourceAsStream(
        "/WEB-INF/ElectricityByCounty.csv"));
    String line = scanner.nextLine();
    while (scanner.hasNextLine()) {
      line = scanner.nextLine();
      String[] cells = line.split(",");
      String county = String.valueOf(cells[0]);
      Double electricity = Double.valueOf(cells[1]);
      electricityMap.put(county, electricity);
    }
    scanner.close();
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("application/json");
    Gson gson = new Gson();
    String json = gson.toJson(electricityMap);
    response.getWriter().println(json);
  }
}
