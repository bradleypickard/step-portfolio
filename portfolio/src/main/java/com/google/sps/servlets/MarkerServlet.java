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

/** Servlet that handles submission and access of comments */
@WebServlet("/marker")
public class MarkerServlet extends HttpServlet {
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("Initiating Query.");
    ArrayList<String> markers = new ArrayList<String>();
    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

    Query query = new Query("Marker");
    PreparedQuery results = datastore.prepare(query);
    for (Entity entity : results.asIterable()) {
      String marker = (String) entity.getProperty("location");
      markers.add(marker);
    }
    System.out.println("Finished Query. Size: " + markers.size());

    writeGetResponse(response, markers);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    System.out.println("Initiating post.");
    String location = request.getParameter("location");
    long timestamp = System.currentTimeMillis();

    Entity markerEntity = new Entity("Marker");
    markerEntity.setProperty("location", location);
    markerEntity.setProperty("timestamp", timestamp);

    DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
    datastore.put(markerEntity);
    System.out.println("Finishing post.");
  }

  private void writeGetResponse(HttpServletResponse response, ArrayList<String> markers)
      throws IOException {
    String json = convertToJsonUsingGson(markers);
    response.setContentType("application/json");
    response.getWriter().println(json);
  }

  /**
   * Converts a comments ArrayList instance into a JSON string using the Gson library.
   */
  private String convertToJsonUsingGson(ArrayList<String> markers) {
    Gson gson = new Gson();
    String json = gson.toJson(markers);
    return json;
  }
}
