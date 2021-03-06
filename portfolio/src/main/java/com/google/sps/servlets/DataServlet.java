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

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.gson.Gson;
import com.google.sps.data.Comment;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {

  ArrayList<Comment> commentArrayList = new ArrayList<Comment>();

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Query commentQuery = new Query("Comment").addSort("timestamp", SortDirection.DESCENDING);
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    PreparedQuery commentResults = datastore.prepare(commentQuery);
    for (Entity commentEntity : commentResults.asIterable()) {
        String commentText = (String) commentEntity.getProperty("comment");
        long timestamp = (long) commentEntity.getProperty("timestamp");
        Comment comment = new Comment(commentText, timestamp);
        commentArrayList.add(comment);
    }
    response.setContentType("application/json");
    Gson gson = new Gson();
    String commentJson = gson.toJson(commentArrayList);
    response.getWriter().println(commentJson);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String comment = request.getParameter("comment-input");
    long timestamp = System.currentTimeMillis();
    if (comment == null || comment.length() == 0) {
        return;
    } else if (comment.length() > 0) {
      Entity commentEntity = new Entity("Comment");
      commentEntity.setProperty("comment", comment);
      commentEntity.setProperty("timestamp", timestamp);
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      datastore.put(commentEntity);
      response.sendRedirect("/");
    }
  }
}
