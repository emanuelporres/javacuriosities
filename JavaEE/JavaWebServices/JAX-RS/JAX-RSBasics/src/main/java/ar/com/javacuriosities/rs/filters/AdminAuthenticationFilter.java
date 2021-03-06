package ar.com.javacuriosities.rs.filters;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

@Provider
public class AdminAuthenticationFilter implements ContainerRequestFilter {

    private static final String AUTHENTICATION_SCHEME = "Basic";

    @Context
    private UriInfo uriInfo;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        boolean isAdminEndpoint = uriInfo.getPath().startsWith("admin");

        Method method = resourceInfo.getResourceMethod();

        // Admin endpoint requires security
        if (isAdminEndpoint) {
            // Access allowed for all
            if (!method.isAnnotationPresent(PermitAll.class)) {
                // Access denied for all
                if (method.isAnnotationPresent(DenyAll.class)) {
                    requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).entity("Access blocked for all users !!").build());
                    return;
                }

                // Get request headers
                final MultivaluedMap<String, String> headers = requestContext.getHeaders();

                // Fetch authorization header
                final List<String> authorization = headers.get(HttpHeaders.AUTHORIZATION);

                // If no authorization information present; block access
                if (authorization == null || authorization.isEmpty()) {
                    requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build());
                    return;
                }

                // Get encoded username and password
                final String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");

                // Decode username and password
                String usernameAndPassword = new String(Base64.getDecoder().decode(encodedUserPassword.getBytes()));

                // Split username and password tokens
                final StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
                final String username = tokenizer.nextToken();
                final String password = tokenizer.nextToken();

                // Verifying Username and password
                System.out.println(username);
                System.out.println(password);

                // Verify user access
                if (method.isAnnotationPresent(RolesAllowed.class)) {
                    RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
                    Set<String> rolesSet = new HashSet<>(Arrays.asList(rolesAnnotation.value()));

                    // Is user valid?
                    if (!isUserAllowed(username, password, rolesSet)) {
                        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("You cannot access this resource").build());
                    }
                }
            }
        }

    }

    private boolean isUserAllowed(final String username, final String password, final Set<String> rolesSet) {
        boolean isAllowed = false;

        // We should fetch username password and assigned roles from DB.

        if (username.equals("admin") && password.equals("123456")) {
            String userRole = "ADMIN";

            //Step 2. Verify user role
            if (rolesSet.contains(userRole)) {
                isAllowed = true;
            }
        }
        return isAllowed;
    }
}
