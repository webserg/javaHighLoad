package com.gmail.webserg.travel.webserver.handler;

import com.gmail.webserg.travel.webserver.DataBase;
import com.networknt.config.Config;
import com.networknt.server.HandlerProvider;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.util.Methods;


public class PathHandlerProvider implements HandlerProvider {

    {
        DataBase.getDb().loadData();
    }

    @Override
    public HttpHandler getHandler() {
        return Handlers.routing()
                .add(Methods.GET, "/users/{id}", new GetUserHandler())
                .add(Methods.GET, "/locations/{id}", new GetLocationHandler())
                .add(Methods.GET, "/visits/{id}", new GetVisitHandler())
                .add(Methods.GET, "/users/{id}/visits", new GetUserVisitsHandler())
                .add(Methods.GET, "/locations/{id}/avg", new GetLocationAvgHandler())
                .add(Methods.POST, "/users/{id}", new PostUpdateUserHandler())
                .add(Methods.POST, "/locations/{id}", new PostUpdateLocationHandler())
                .add(Methods.POST, "/visits/{id}", new PostUpdateVisitHandler())
                .add(Methods.POST, "/users/new", new PostNewUserHandler())
                .add(Methods.POST, "/locations/new", new PostNewLocationHandler())
                .add(Methods.POST, "/visits/new", new PostNewVisitHandler());
    }
}
