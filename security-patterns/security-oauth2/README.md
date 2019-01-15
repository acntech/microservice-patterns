# OAuth 2 Security Pattern
This module explores the use of Spring OAuth2 security to protect REST API.

### OAuth 2 Roles
* __Resource Owner__: Generally yourself.
* __Resource Server__: Server hosting protected data (for example Google hosting your profile and
                       personal information).
* __Client__: Application requesting access to a resource server ( website, a Javascript application
              or a mobile application…).
* __Authorization Server__: Server issuing access token to the client. This token will be used for
                            the client to request the resource server. This server can be the same
                            as the resource server (same physical server and same application), and
                            it is often the case.

### OAuth 2 Grant Types
* __Implicit__: The implicit grant is a simplified authorization code flow optimized for clients
                implemented in a browser using a scripting language such as JavaScript. In the
                implicit flow, instead of issuing the client an authorization code, the client is
                issued an access token directly.
* __Password__: It is intended to be used for user-agent-based clients (e.g. single page web apps)
                that can’t keep a client secret.Secondly instead of the authorization server
                returning an authorization code which is exchanged for an access token as the case
                of Authorization Code grant, the authorization server returns an access token.
* __Client Credentials__: The client can request an access token using only its client credentials
                          (or other supported means of authentication) when the client is
                          requesting access to the protected resources under its control, or those
                          of another resource owner that have been previously arranged with the
                          authorization server.
* __Refresh Token__:
* __Authorization Code__: The authorization code grant is the feature of signing into an application
                          using your Facebook or Google account.
