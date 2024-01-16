package org.acme;

import io.quarkus.grpc.ClientGreeter;
import io.quarkus.grpc.GreeterRequest;
import io.quarkus.grpc.GreeterResponse;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/greet")
public class GreetingResource {

    @GrpcClient
    ClientGreeter clientGreeter;

    @GET
    @Path("/{name}")
    public Uni<String> greet(String name) {
        return clientGreeter.greetClient(GreeterRequest.newBuilder().setClientName(name).build())
                .onItem().transform(GreeterResponse::getMessage);
    }
}
