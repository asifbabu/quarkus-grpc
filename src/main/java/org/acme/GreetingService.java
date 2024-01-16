package org.acme;

import com.flydubai.hellosoap.GreetPort;
import com.flydubai.hellosoap.HelloSoapRequest;
import com.flydubai.hellosoap.ObjectFactory;
import io.quarkiverse.cxf.annotation.CXFClient;
import io.quarkus.grpc.ClientGreeter;
import io.quarkus.grpc.GreeterRequest;
import io.quarkus.grpc.GreeterResponse;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;

@GrpcService
public class GreetingService implements ClientGreeter {
    @CXFClient("greetPort")
    GreetPort greetPort;

    @Override
    public Uni<GreeterResponse> greetClient(GreeterRequest request) {
        ObjectFactory factory = new ObjectFactory();
        HelloSoapRequest soapRequest = factory.createHelloSoapRequest();
        soapRequest.setClientName(request.getClientName());
        return Uni.createFrom().item(() -> greetPort.helloSoap(soapRequest))
                .map(helloSoapResponse ->
                        getGreeterResponse(helloSoapResponse.getResponse()))
                .onFailure().recoverWithItem(getGreeterResponse("An error occurred while fetching the greeting message"));
    }

    private static GreeterResponse getGreeterResponse(String helloSoapResponse) {
        return GreeterResponse.newBuilder().setMessage(helloSoapResponse.replace("Welcome","Hello")).build();
    }
}
