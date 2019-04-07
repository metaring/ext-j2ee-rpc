/**
 *    Copyright 2019 MetaRing s.r.l.
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.metaring.framework.ext.j2ee.rpc;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.metaring.framework.rpc.CallFunctionalityImpl;

@WebServlet("/call")
public class J2EEWebServicesController extends HttpServlet {

    private static final long serialVersionUID = -8337655729844820209L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader reader = request.getReader()) {
            String line;
            while((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        CallFunctionalityImpl.execute((long)(serialVersionUID * System.currentTimeMillis() * Math.random()), request.getRemoteAddr(), null, null, true, sb.toString()).whenComplete((result, error) -> {
            if(error != null) {
                error.printStackTrace();
                return;
            }
            response.setStatus(HttpServletResponse.SC_OK);
            try {
                response.getWriter().println(result.toJson());
            }
            catch (IOException e) {
            }
        });
    }
}