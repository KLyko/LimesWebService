
/**
 * LimesServiceImplCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.6.2  Built on : Apr 17, 2012 (05:33:49 IST)
 */

    package de.uni_leipzig.simba.limeswebservice.server;

    /**
     *  LimesServiceImplCallbackHandler Callback class, Users can extend this class and implement
     *  their own receiveResult and receiveError methods.
     */
    public abstract class LimesServiceImplCallbackHandler{



    protected Object clientData;

    /**
    * User can pass in any object that needs to be accessed once the NonBlocking
    * Web service call is finished and appropriate method of this CallBack is called.
    * @param clientData Object mechanism by which the user can pass in user data
    * that will be avilable at the time this callback is called.
    */
    public LimesServiceImplCallbackHandler(Object clientData){
        this.clientData = clientData;
    }

    /**
    * Please use this constructor if you don't want to set any clientData
    */
    public LimesServiceImplCallbackHandler(){
        this.clientData = null;
    }

    /**
     * Get the client data
     */

     public Object getClientData() {
        return clientData;
     }

        
           /**
            * auto generated Axis2 call back method for fetchMetricMap method
            * override this method for handling normal response from fetchMetricMap operation
            */
           public void receiveResultfetchMetricMap(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchMetricMapResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fetchMetricMap operation
           */
            public void receiveErrorfetchMetricMap(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for fetchSourceData method
            * override this method for handling normal response from fetchSourceData operation
            */
           public void receiveResultfetchSourceData(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchSourceDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fetchSourceData operation
           */
            public void receiveErrorfetchSourceData(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for fetchTargetData method
            * override this method for handling normal response from fetchTargetData operation
            */
           public void receiveResultfetchTargetData(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.FetchTargetDataResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from fetchTargetData operation
           */
            public void receiveErrorfetchTargetData(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
           /**
            * auto generated Axis2 call back method for startSession method
            * override this method for handling normal response from startSession operation
            */
           public void receiveResultstartSession(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.StartSessionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from startSession operation
           */
            public void receiveErrorstartSession(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for getMetricAdvice method
            * override this method for handling normal response from getMetricAdvice operation
            */
           public void receiveResultgetMetricAdvice(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.GetMetricAdviceResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from getMetricAdvice operation
           */
            public void receiveErrorgetMetricAdvice(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for learnMetric method
            * override this method for handling normal response from learnMetric operation
            */
           public void receiveResultlearnMetric(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.LearnMetricResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from learnMetric operation
           */
            public void receiveErrorlearnMetric(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for polling method
            * override this method for handling normal response from polling operation
            */
           public void receiveResultpolling(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.PollingResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from polling operation
           */
            public void receiveErrorpolling(java.lang.Exception e) {
            }
                
           /**
            * auto generated Axis2 call back method for continueSession method
            * override this method for handling normal response from continueSession operation
            */
           public void receiveResultcontinueSession(
                    de.uni_leipzig.simba.limeswebservice.server.LimesServiceImplStub.ContinueSessionResponse result
                        ) {
           }

          /**
           * auto generated Axis2 Error handler
           * override this method for handling error response from continueSession operation
           */
            public void receiveErrorcontinueSession(java.lang.Exception e) {
            }
                
               // No methods generated for meps other than in-out
                
               // No methods generated for meps other than in-out
                


    }
    