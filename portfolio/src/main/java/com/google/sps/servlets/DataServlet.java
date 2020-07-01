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
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.ReadPolicy;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Servlet that returns some example content. TODO: modify this file to handle comments data */
@WebServlet("/data")
public class DataServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("Initiating Query.");
    ArrayList<String> comments = new ArrayList<String>();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Comment");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String comment = (String) entity.getProperty("body");
      comments.add(comment);
    }
    System.out.println("Finished Query. Size: " + comments.size());

    String maxCommentParam = request.getParameter("max-comments");
    int displayedComments = tryParseInt(maxCommentParam);

    if (displayedComments >= 0)
      writeGetResponse(response, comments, displayedComments);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("Initiating post.");
    String commentBody = request.getParameter("comment-body");

    Entity commentEntity = new Entity("Comment");
    commentEntity.setProperty("body", commentBody);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(commentEntity);
    System.out.println("Finishing post.");
  }

  private int tryParseInt(String str) {
    int num;
    try {
      num = Integer.parseInt(str);
    } catch (NumberFormatException e) {
      System.err.println("Invalid max comment parameter: " + str);
      return -1;
    }
    return num;
  }

  private void writeGetResponse(HttpServletResponse response, ArrayList<String> comments,
      int displayedComments) throws IOException {
    if (displayedComments > comments.size())
      displayedComments = comments.size();

    ArrayList subList = new ArrayList<String>(comments.subList(0, displayedComments));
    String json = convertToJsonUsingGson(subList);
    response.setContentType("application/json;");
    response.getWriter().println(json);
  }

  /**
   * Converts a comments ArrayList instance into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson(ArrayList<String> comments) {
    Gson gson = new Gson();
    String json = gson.toJson(comments);
    return json;
  }
}
