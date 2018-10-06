package Karma.query;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.Random;

import org.apache.xmlbeans.XmlAnyURI;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlString;
import org.dataandsearch.www.karma._2010._08.ActionEnumType;
import org.dataandsearch.www.karma._2010._08.AnnotationType;
import org.dataandsearch.www.karma._2010._08.BlockType;
import org.dataandsearch.www.karma._2010._08.DataConsumedDocument;
import org.dataandsearch.www.karma._2010._08.DataDeletedDocument;
import org.dataandsearch.www.karma._2010._08.DataLifeCycleType;
import org.dataandsearch.www.karma._2010._08.DataObjectType;
import org.dataandsearch.www.karma._2010._08.DataProducedDocument;
import org.dataandsearch.www.karma._2010._08.DataReceivedFinishedDocument;
import org.dataandsearch.www.karma._2010._08.DataReceivedStartedDocument;
import org.dataandsearch.www.karma._2010._08.DataSendFinishedDocument;
import org.dataandsearch.www.karma._2010._08.DataSendStartedDocument;
import org.dataandsearch.www.karma._2010._08.DataTransferStatusType;
import org.dataandsearch.www.karma._2010._08.DataTransferType;
import org.dataandsearch.www.karma._2010._08.DataUpdatedDocument;
import org.dataandsearch.www.karma._2010._08.EntityEnumType;
import org.dataandsearch.www.karma._2010._08.EntityType;
import org.dataandsearch.www.karma._2010._08.InvocationStatusType;
import org.dataandsearch.www.karma._2010._08.InvocationType;
import org.dataandsearch.www.karma._2010._08.InvokingServiceDocument;
import org.dataandsearch.www.karma._2010._08.InvokingServiceStatusDocument;
import org.dataandsearch.www.karma._2010._08.InvokingWorkflowDocument;
import org.dataandsearch.www.karma._2010._08.InvokingWorkflowStatusDocument;
import org.dataandsearch.www.karma._2010._08.KarmaServiceStub;
import org.dataandsearch.www.karma._2010._08.ReceivedFaultDocument;
import org.dataandsearch.www.karma._2010._08.ReceivedResponseDocument;
import org.dataandsearch.www.karma._2010._08.SendingFaultDocument;
import org.dataandsearch.www.karma._2010._08.SendingResponseDocument;
import org.dataandsearch.www.karma._2010._08.SendingResponseStatusDocument;
import org.dataandsearch.www.karma._2010._08.ServiceInformationType;
import org.dataandsearch.www.karma._2010._08.ServiceInvokedDocument;
import org.dataandsearch.www.karma._2010._08.StatusEnumType;
import org.dataandsearch.www.karma._2010._08.WorkflowInformationType;
import org.dataandsearch.www.karma._2010._08.WorkflowInvokedDocument;
import org.dataandsearch.www.karma.query._2010._10.DataProductIDListType;
import org.dataandsearch.www.karma.query._2010._10.DetailEnumType;
import org.dataandsearch.www.karma.query._2010._10.FormatEnumType;
import org.dataandsearch.www.karma.query._2010._10.GetDataProductDetailRequestDocument;
import org.dataandsearch.www.karma.query._2010._10.GetDataProductDetailRequestType;
import org.dataandsearch.www.karma.query._2010._10.GetDataProductDetailResponseDocument;
import org.dataandsearch.www.karma.query._2010._10.GetProvenanceHistoryRequestDocument;
import org.dataandsearch.www.karma.query._2010._10.GetProvenanceHistoryRequestType;
import org.dataandsearch.www.karma.query._2010._10.GetProvenanceHistoryResponseDocument;
import org.dataandsearch.www.karma.query._2010._10.GetServiceDetailRequestDocument;
import org.dataandsearch.www.karma.query._2010._10.GetServiceDetailRequestType;
import org.dataandsearch.www.karma.query._2010._10.GetServiceDetailResponseDocument;
import org.dataandsearch.www.karma.query._2010._10.GetWorkflowGraphRequestDocument;
import org.dataandsearch.www.karma.query._2010._10.GetWorkflowGraphRequestType;
import org.dataandsearch.www.karma.query._2010._10.GetWorkflowGraphResponseDocument;
import org.dataandsearch.www.karma.query._2010._10.UniqueIDListType;
import org.dataandsearch.www.karma.query._2010._10.UniqueURIListType;

/**
 * @author Yiming Sun
 * @author Devarshi Ghoshal
 * @author You-Wei Cheah
 * @author Peng Chen
 */
public class KarmaAxis2Query {

	private static String constServiceID = "urn:qname:http://www.d2i.indiana.edu/karma:TestService_";
	private static String constWorkflowID = "tag:www.d2i.indiana.edu/TestWorkflow/instance";
	private static String constWorkflowNodeID = "WFNode:KarmaProcessor";
	// change to desired URI
	private static String contextWorkflowURI = "sea-ice-processing-20101117181942";
	// change to desired artifact ID
	private static String artifactID = "Block_10";

	// private static String constFileURI =
	// "http://www.d2i.indiana.edu/testKarmaFile";
	// private static String constDN = "KarmaTester";

	private static String constProperty = "execution_environment";
	private static String constValue = "POSIX";

	private static int ramanujanNum = 1729;

	// NotificationTester tester = null;
	KarmaServiceStub stub = null;

	public KarmaAxis2Query(String serviceURL) throws Exception {
		// this.tester = tester;
		// stub = new
		// KarmaServiceStub("http://127.0.0.1:20032/axis2/services/KarmaService?wsdl");
		stub = new KarmaServiceStub(serviceURL);
	}

	private static enum EntityTypeEnum {
		WORKFLOW, SERVICE;
	}

	private static enum DataTransferTypeEnum {
		RESULT, FAULT;
	}

	private void addEntityInformation(EntityType entity,
			EntityTypeEnum entityType) {

		Random randomNumGen = new Random();
		int id = randomNumGen.nextInt(ramanujanNum);

		String workflowID = constWorkflowID + id;
		String workflowNodeID = constWorkflowNodeID + id;
		int timestep = id;

		if (entityType.equals(EntityTypeEnum.SERVICE)) {
			ServiceInformationType entityServiceInfoType = entity
					.addNewServiceInformation();
			entityServiceInfoType.setWorkflowID(workflowID);
			entityServiceInfoType.setWorkflowNodeID(workflowNodeID);
			entityServiceInfoType.setTimestep(timestep);

			String serviceID = constServiceID + id;
			entityServiceInfoType.setServiceID(serviceID);
			entity.setType(EntityEnumType.SERVICE);
		} else {
			WorkflowInformationType entityWorkflowInfoType = entity
					.addNewWorkflowInformation();
			entityWorkflowInfoType.setWorkflowID(workflowID);
			entityWorkflowInfoType.setWorkflowNodeID(workflowNodeID);
			entityWorkflowInfoType.setTimestep(timestep);

			entity.setType(EntityEnumType.WORKFLOW);
		}

	}

	private void addDataObject(DataObjectType params) {

		/*
		 * Random randomNumGen = new Random(); int id =
		 * randomNumGen.nextInt(ramanujanNum);
		 * 
		 * String fileURI = constFileURI + id; String ownerDN =
		 * Integer.toString(id); long size = 0;
		 * 
		 * FileType file = params.addNewFile(); file.setFileURI(fileURI);
		 * file.setOwnerDN(ownerDN); file.setSize(size);
		 * file.setCreateDate(Calendar.getInstance());
		 */

		BlockType block3 = params.addNewBlock();
		try {
			block3.setBlockContent(XmlObject.Factory
					.parse("<block>name=value</block>"));
		} catch (XmlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addAnnotations(AnnotationType[] annotations, String property,
			String value) {
		String xmlChunk = "<value>" + value + "</value>";
		/*
		 * try { annotations.setProperty(property);
		 * annotations.set(XmlObject.Factory.parse(xmlChunk)); } catch
		 * (XmlException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		for (int i = 0; i < annotations.length; i++) {
			annotations[i] = AnnotationType.Factory.newInstance();
			annotations[i].setProperty(property);
			try {
				annotations[i].setValue(XmlObject.Factory.parse(xmlChunk));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private InvokingServiceDocument makeInvokingService() {
		InvokingServiceDocument invokingServiceDoc = InvokingServiceDocument.Factory
				.newInstance();
		InvocationType invocationType = invokingServiceDoc
				.addNewInvokingService();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		invocationHelper(invocationType, entityType);
		return invokingServiceDoc;
	}

	private InvokingServiceStatusDocument makeInvokingServiceStatus() {
		InvokingServiceStatusDocument invokingServiceStatusDoc = InvokingServiceStatusDocument.Factory
				.newInstance();
		InvocationStatusType invocationStatusType = invokingServiceStatusDoc
				.addNewInvokingServiceStatus();

		invocationStatusType.setStatus(StatusEnumType.SUCCESS);
		invocationStatusType.setStatusTime(Calendar.getInstance());

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;
		invocationHelper(invocationStatusType, entityType);
		return invokingServiceStatusDoc;
	}

	private ServiceInvokedDocument makeServiceInvoked() {
		ServiceInvokedDocument serviceInvokedDoc = ServiceInvokedDocument.Factory
				.newInstance();
		InvocationType invocationType = serviceInvokedDoc
				.addNewServiceInvoked();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;
		invocationHelper(invocationType, entityType);

		return serviceInvokedDoc;
	}

	private InvokingWorkflowDocument makeInvokingWorkflow() {
		InvokingWorkflowDocument invokingWorkflowDoc = InvokingWorkflowDocument.Factory
				.newInstance();
		InvocationType invocationType = invokingWorkflowDoc
				.addNewInvokingWorkflow();

		EntityTypeEnum entityType = EntityTypeEnum.WORKFLOW;
		invocationHelper(invocationType, entityType);
		return invokingWorkflowDoc;

	}

	private InvokingWorkflowStatusDocument makeInvokingWorkflowStatus() {
		InvokingWorkflowStatusDocument invokingWorkflowStatusDoc = InvokingWorkflowStatusDocument.Factory
				.newInstance();
		InvocationStatusType invocationStatusType = invokingWorkflowStatusDoc
				.addNewInvokingWorkflowStatus();
		invocationStatusType.setStatus(StatusEnumType.SUCCESS);
		invocationStatusType.setStatusTime(Calendar.getInstance());

		EntityTypeEnum entityType = EntityTypeEnum.WORKFLOW;
		invocationHelper(invocationStatusType, entityType);
		return invokingWorkflowStatusDoc;
	}

	private WorkflowInvokedDocument makeWorkflowInvoked() {
		WorkflowInvokedDocument workflowInvokedDoc = WorkflowInvokedDocument.Factory
				.newInstance();
		InvocationType invocationType = workflowInvokedDoc
				.addNewWorkflowInvoked();

		EntityTypeEnum entityType = EntityTypeEnum.WORKFLOW;
		invocationHelper(invocationType, entityType);
		return workflowInvokedDoc;
	}

	private void invocationHelper(InvocationType invocationType,
			EntityTypeEnum entityType) {

		invocationType.setInvocationTime(Calendar.getInstance());

		EntityType invoker = invocationType.addNewInvoker();
		EntityType invokee = invocationType.addNewInvokee();
		DataObjectType params = invocationType.addNewParams();
		// AnnotationType annotations = invocationType.addNewAnnotations();

		addEntityInformation(invoker, entityType);

		addEntityInformation(invokee, entityType);

		addDataObject(params);

		// String property = constProperty;
		// String value = constValue;
		// addAnnotations(annotations, property, value);
		AnnotationType[] annotations = new AnnotationType[1];
		addAnnotations(annotations, constProperty, constValue);
		invocationType.setAnnotationsArray(annotations);
	}

	private void invocationHelper(InvocationStatusType invocationType,
			EntityTypeEnum entityType) {

		invocationType.setInvocationTime(Calendar.getInstance());
		invocationType.setStatus(StatusEnumType.SUCCESS);
		invocationType.setStatusTime(Calendar.getInstance());

		EntityType invoker = invocationType.addNewInvoker();
		EntityType invokee = invocationType.addNewInvokee();
		DataObjectType params = invocationType.addNewParams();
		// AnnotationType annotations = invocationType.addNewAnnotations();

		addEntityInformation(invoker, entityType);

		addEntityInformation(invokee, entityType);

		addDataObject(params);

		// String property = constProperty;
		// String value = constValue;
		// addAnnotations(annotations, property, value);
		AnnotationType[] annotations = new AnnotationType[1];
		addAnnotations(annotations, constProperty, constValue);
		invocationType.setAnnotationsArray(annotations);
	}

	private DataProducedDocument makeDataProduced() {
		DataProducedDocument dataProducedDoc = DataProducedDocument.Factory
				.newInstance();
		DataLifeCycleType dataLifeCycle = dataProducedDoc.addNewDataProduced();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		dataLifeCycle.setAction(ActionEnumType.PRODUCE);

		dataLifeCycleHelper(dataLifeCycle, entityType);
		return dataProducedDoc;
	}

	private DataConsumedDocument makeDataConsumed() {
		DataConsumedDocument dataConsumedDoc = DataConsumedDocument.Factory
				.newInstance();
		DataLifeCycleType dataLifeCycle = dataConsumedDoc.addNewDataConsumed();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		dataLifeCycle.setAction(ActionEnumType.CONSUME);

		dataLifeCycleHelper(dataLifeCycle, entityType);

		return dataConsumedDoc;
	}

	private DataDeletedDocument makeDataDeleted() {
		DataDeletedDocument dataDeletedDoc = DataDeletedDocument.Factory
				.newInstance();
		DataLifeCycleType dataLifeCycle = dataDeletedDoc.addNewDataDeleted();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		dataLifeCycle.setAction(ActionEnumType.DELETE);

		dataLifeCycleHelper(dataLifeCycle, entityType);
		return dataDeletedDoc;
	}

	private DataUpdatedDocument makeDataUpdated() {
		DataUpdatedDocument dataUpdatedDoc = DataUpdatedDocument.Factory
				.newInstance();
		DataLifeCycleType dataLifeCycle = dataUpdatedDoc.addNewDataUpdated();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		dataLifeCycle.setAction(ActionEnumType.UPDATE);

		dataLifeCycleHelper(dataLifeCycle, entityType);
		return dataUpdatedDoc;
	}

	/**
	 * *Helper method for Data Lifecycle Tests*
	 * 
	 * @param dataLifeCycle
	 * @param entityType
	 */
	private void dataLifeCycleHelper(DataLifeCycleType dataLifeCycle,
			EntityTypeEnum entityType) {
		EntityType actor = dataLifeCycle.addNewActor();
		DataObjectType dataObject = dataLifeCycle.addNewDataObject();

		dataLifeCycle.setTimestamp(Calendar.getInstance());

		addEntityInformation(actor, entityType);

		addDataObject(dataObject);

		AnnotationType[] annotations = new AnnotationType[1];
		addAnnotations(annotations, constProperty, constValue);
		dataLifeCycle.setAnnotationsArray(annotations);
	}

	private DataSendStartedDocument makeDataSendStarted() {
		DataSendStartedDocument dataSendStartedDoc = DataSendStartedDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = dataSendStartedDoc
				.addNewDataSendStarted();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		DataTransferTypeEnum transferType = DataTransferTypeEnum.RESULT;
		dataTransferHelper(dataTransferType, entityType, transferType);
		return dataSendStartedDoc;
	}

	private DataSendFinishedDocument makeDataSendFinished() {
		DataSendFinishedDocument dataSendFinishedDoc = DataSendFinishedDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = dataSendFinishedDoc
				.addNewDataSendFinished();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		DataTransferTypeEnum transferType = DataTransferTypeEnum.RESULT;
		dataTransferHelper(dataTransferType, entityType, transferType);
		return dataSendFinishedDoc;
	}

	DataReceivedStartedDocument makeDataReceivedStarted() {
		DataReceivedStartedDocument dataReceivedStartedDoc = DataReceivedStartedDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = dataReceivedStartedDoc
				.addNewDataReceivedStarted();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		DataTransferTypeEnum transferType = DataTransferTypeEnum.FAULT;
		dataTransferHelper(dataTransferType, entityType, transferType);
		return dataReceivedStartedDoc;

	}

	DataReceivedFinishedDocument makeDataReceivedFinished() {
		DataReceivedFinishedDocument dataReceiveFinishedDoc = DataReceivedFinishedDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = dataReceiveFinishedDoc
				.addNewDataReceivedFinished();

		EntityTypeEnum entityType = EntityTypeEnum.SERVICE;

		DataTransferTypeEnum transferType = DataTransferTypeEnum.FAULT;
		dataTransferHelper(dataTransferType, entityType, transferType);
		return dataReceiveFinishedDoc;
	}

	private ReceivedResponseDocument makeReceivedResponse() {
		ReceivedResponseDocument receivedResponsedoc = ReceivedResponseDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = receivedResponsedoc
				.addNewReceivedResponse();
		dataTransferHelper(dataTransferType, EntityTypeEnum.SERVICE,
				DataTransferTypeEnum.RESULT);
		return receivedResponsedoc;
	}

	private SendingResponseStatusDocument makeSendingResponseStatus() {
		SendingResponseStatusDocument sendingResponseStatusDoc = SendingResponseStatusDocument.Factory
				.newInstance();
		DataTransferStatusType sendingResponseStatus = sendingResponseStatusDoc
				.addNewSendingResponseStatus();
		dataTransferHelper(sendingResponseStatus, EntityTypeEnum.SERVICE,
				DataTransferTypeEnum.RESULT);
		sendingResponseStatus.setStatus(StatusEnumType.SUCCESS);
		sendingResponseStatus.setStatusTime(Calendar.getInstance());
		return sendingResponseStatusDoc;
	}

	private SendingResponseDocument makeSendingResponse() {
		SendingResponseDocument sendingResponseDoc = SendingResponseDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = sendingResponseDoc
				.addNewSendingResponse();
		dataTransferHelper(dataTransferType, EntityTypeEnum.SERVICE,
				DataTransferTypeEnum.RESULT);
		return sendingResponseDoc;
	}

	ReceivedFaultDocument makeReceivedFault() {
		ReceivedFaultDocument receivedFaultDoc = ReceivedFaultDocument.Factory
				.newInstance();
		DataTransferType dataTransferType = receivedFaultDoc
				.addNewReceivedFault();
		dataTransferHelper(dataTransferType, EntityTypeEnum.SERVICE,
				DataTransferTypeEnum.FAULT);
		return receivedFaultDoc;
	}

	private SendingFaultDocument makeSendingFault() {
		SendingFaultDocument sendingFaultDoc = SendingFaultDocument.Factory
				.newInstance();
		DataTransferType sendingFault = sendingFaultDoc.addNewSendingFault();
		dataTransferHelper(sendingFault, EntityTypeEnum.SERVICE,
				DataTransferTypeEnum.FAULT);
		return sendingFaultDoc;
	}

	/**
	 * *Helper Method for Data Transfer Tests*
	 * 
	 * @param dataTransferType
	 * @param entityType
	 * @param transferType
	 */
	private void dataTransferHelper(DataTransferType dataTransferType,
			EntityTypeEnum entityType, DataTransferTypeEnum transferType) {
		EntityType sender = dataTransferType.addNewSender();
		EntityType receiver = dataTransferType.addNewReceiver();

		dataTransferType.setTransferTime(Calendar.getInstance());

		DataObjectType dataObject = null;
		if (transferType.equals(DataTransferTypeEnum.RESULT)) {
			dataObject = dataTransferType.addNewResult();
		} else if (transferType.equals(DataTransferTypeEnum.FAULT)) {
			dataObject = dataTransferType.addNewFault();
		}

		addEntityInformation(sender, entityType);
		addEntityInformation(receiver, entityType);

		addDataObject(dataObject);

		AnnotationType[] annotations = new AnnotationType[1];
		addAnnotations(annotations, constProperty, constValue);
		dataTransferType.setAnnotationsArray(annotations);
	}

	public void testInvokingWorkflow() {
		InvokingWorkflowDocument invokingWorkflow = makeInvokingWorkflow();
		try {
			stub.invokingWorkflow(invokingWorkflow);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);

		}
	}

	public void testInvokingWorkflowStatus() {
		InvokingWorkflowStatusDocument invokingWorkflowStatus = makeInvokingWorkflowStatus();
		try {
			stub.invokingWorkflowStatus(invokingWorkflowStatus);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}

	}

	public void testWorkflowInvoked() {
		WorkflowInvokedDocument workflowInvoked = makeWorkflowInvoked();
		try {
			stub.workflowInvoked(workflowInvoked);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testInvokingService() {
		InvokingServiceDocument invokingService = makeInvokingService();
		try {
			stub.invokingService(invokingService);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testInvokingServiceStatus() {
		InvokingServiceStatusDocument invokingServiceStatus = makeInvokingServiceStatus();
		try {
			stub.invokingServiceStatus(invokingServiceStatus);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testServiceInvoked() {
		ServiceInvokedDocument serviceInvoked = makeServiceInvoked();
		try {
			stub.serviceInvoked(serviceInvoked);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataProduced() {
		DataProducedDocument dataProduced = makeDataProduced();
		try {
			stub.dataProduced(dataProduced);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataConsumed() {
		DataConsumedDocument dataConsumed = makeDataConsumed();
		try {
			stub.dataConsumed(dataConsumed);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataDeleted() {
		DataDeletedDocument dataDeleted = makeDataDeleted();
		try {
			stub.dataDeleted(dataDeleted);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataUpdated() {
		DataUpdatedDocument dataUpdated = makeDataUpdated();
		try {
			stub.dataUpdated(dataUpdated);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataSendStarted() {
		DataSendStartedDocument dataSendStarted = makeDataSendStarted();
		try {
			stub.dataSendStarted(dataSendStarted);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataSendFinished() {
		DataSendFinishedDocument dataSendFinished = makeDataSendFinished();
		try {
			stub.dataSendFinished(dataSendFinished);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataReceiveFinished() {
		DataReceivedFinishedDocument dataReceivedFinished = makeDataReceivedFinished();
		try {
			stub.dataReceivedFinished(dataReceivedFinished);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testDataReceiveStarted() {
		DataReceivedStartedDocument dataReceivedStarted = makeDataReceivedStarted();
		try {
			stub.dataReceivedStarted(dataReceivedStarted);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testReceivedResponse() {
		ReceivedResponseDocument receivedResponse = makeReceivedResponse();
		try {
			stub.receivedResponse(receivedResponse);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testSendingResponseStatus() {
		SendingResponseStatusDocument sendingResponseStatus = makeSendingResponseStatus();
		try {
			stub.sendingResponseStatus(sendingResponseStatus);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}

	}

	public void testSendingResponse() {
		SendingResponseDocument sendingResponse = makeSendingResponse();
		try {
			stub.sendingResponse(sendingResponse);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testReceivedFault() {
		ReceivedFaultDocument receivedFault = makeReceivedFault();
		try {
			stub.receivedFault(receivedFault);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	public void testSendingFault() {
		SendingFaultDocument sendingFault = makeSendingFault();
		try {
			stub.sendingFault(sendingFault);
			System.out.println("[OK]");
		} catch (Exception e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
	}

	// public String getWorkflowGraph(String contextWorkflowURI) {
	//
	// GetWorkflowGraphRequestDocument getWorkflowGraphRequestDocument =
	// GetWorkflowGraphRequestDocument.Factory
	// .newInstance();
	//
	// GetWorkflowGraphRequestType getWorkflowGraphRequest =
	// getWorkflowGraphRequestDocument
	// .addNewGetWorkflowGraphRequest();
	// getWorkflowGraphRequest.setWorkflowID(contextWorkflowURI);
	// getWorkflowGraphRequest.setFormat(FormatEnumType.OPM);
	// //getWorkflowGraphRequest.setInformationDetailLevel(DetailEnumType.FINE);
	// // optional
	// // parameter
	//
	// try {
	// GetWorkflowGraphResponseDocument workflowGraph = stub
	// .getWorkflowGraph(getWorkflowGraphRequestDocument);
	// // System.out.println("[OK]");
	// // System.out.println(workflowGraph.xmlText());
	// return workflowGraph.xmlText();
	// } catch (RemoteException e) {
	// // System.out.println("[FAILED]");
	// e.printStackTrace(System.err);
	// return "[FAILED]";
	// }
	// }

	public String getWorkflowGraphWithAnnotation(String contextWorkflowURI) {

		GetWorkflowGraphRequestDocument getWorkflowGraphRequestDocument = GetWorkflowGraphRequestDocument.Factory
				.newInstance();

		GetWorkflowGraphRequestType getWorkflowGraphRequest = getWorkflowGraphRequestDocument
				.addNewGetWorkflowGraphRequest();
		getWorkflowGraphRequest.setWorkflowID(contextWorkflowURI);
		getWorkflowGraphRequest.setFormat(FormatEnumType.OPM);
		getWorkflowGraphRequest.setInformationDetailLevel(DetailEnumType.FINE);

		try {
			GetWorkflowGraphResponseDocument workflowGraph = stub
					.getWorkflowGraph(getWorkflowGraphRequestDocument);
			// System.out.println("[OK]");
			// System.out.println(workflowGraph.xmlText());
			return workflowGraph.xmlText();
		} catch (RemoteException e) {
			// System.out.println("[FAILED]");
			e.printStackTrace(System.err);
			return "[FAILED]";
		}
	}

	public String getWorkflowGraphWithoutAnnotation(String contextWorkflowURI) {

		GetWorkflowGraphRequestDocument getWorkflowGraphRequestDocument = GetWorkflowGraphRequestDocument.Factory
				.newInstance();

		GetWorkflowGraphRequestType getWorkflowGraphRequest = getWorkflowGraphRequestDocument
				.addNewGetWorkflowGraphRequest();
		getWorkflowGraphRequest.setWorkflowID(contextWorkflowURI);
		getWorkflowGraphRequest.setFormat(FormatEnumType.OPM);
		getWorkflowGraphRequest
				.setInformationDetailLevel(DetailEnumType.COARSE);
		// // optional
		// parameter

		try {
			GetWorkflowGraphResponseDocument workflowGraph = stub
					.getWorkflowGraph(getWorkflowGraphRequestDocument);
			// System.out.println("[OK]");
			// System.out.println(workflowGraph.xmlText());
			return workflowGraph.xmlText();
		} catch (RemoteException e) {
			// System.out.println("[FAILED]");
			e.printStackTrace(System.err);
			return "[FAILED]";
		}
	}

	public String getProvenanceHistory(String ID) {
		GetProvenanceHistoryRequestDocument provenanceHistoryRequestDocument = GetProvenanceHistoryRequestDocument.Factory
				.newInstance();
		GetProvenanceHistoryRequestType provenanceHistoryRequest = provenanceHistoryRequestDocument
				.addNewGetProvenanceHistoryRequest();
		provenanceHistoryRequest.setArtifactID(ID);
		provenanceHistoryRequest.setFormat(FormatEnumType.OPM);
		provenanceHistoryRequest.setTimeRange(0); // optional parameter
		provenanceHistoryRequest.setInformationDetailLevel(DetailEnumType.FINE); // optional
		// parameter

		try {
			GetProvenanceHistoryResponseDocument provenanceHistory = stub
					.getProvenanceHistory(provenanceHistoryRequestDocument);
			System.out.println("[OK]");
			return provenanceHistory.xmlText();
		} catch (RemoteException e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);
		}
		return null;
	}

	public String getServiceDetailByID(String[] ServiceID) {
		GetServiceDetailRequestDocument getServiceDetailRequestDocument = GetServiceDetailRequestDocument.Factory
				.newInstance();
		GetServiceDetailRequestType getServiceDetailRequest = getServiceDetailRequestDocument
				.addNewGetServiceDetailRequest();
		UniqueIDListType uniqueURIList = getServiceDetailRequest
				.addNewUniqueIDList();

		for (int i = 0; i < ServiceID.length; i++) {
			XmlString uniqueURI = uniqueURIList.addNewUniqueID();
			uniqueURI.setStringValue(ServiceID[i]);
			System.out.println(ServiceID[i]);
		}

		try {
			GetServiceDetailResponseDocument serviceDetail = stub
					.getServiceDetail(getServiceDetailRequestDocument);
			// System.out.println(getServiceDetailRequestDocument.toString());
			System.out.println("[OK]");
			// System.out.println(serviceDetail);

			return serviceDetail.toString();
		} catch (RemoteException e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);

			return null;
		}
	}

	public String getServiceDetailByURI(String[] ServiceID) {
		GetServiceDetailRequestDocument getServiceDetailRequestDocument = GetServiceDetailRequestDocument.Factory
				.newInstance();
		GetServiceDetailRequestType getServiceDetailRequest = getServiceDetailRequestDocument
				.addNewGetServiceDetailRequest();
		UniqueURIListType uniqueURIList = getServiceDetailRequest
				.addNewUniqueURIList();

		for (int i = 0; i < ServiceID.length; i++) {
			XmlAnyURI uniqueURI = uniqueURIList.addNewUniqueURI();
			uniqueURI.setStringValue(ServiceID[i]);
			System.out.println(ServiceID[i]);
		}

		try {
			GetServiceDetailResponseDocument serviceDetail = stub
					.getServiceDetail(getServiceDetailRequestDocument);
			// System.out.println(getServiceDetailRequestDocument.toString());
			System.out.println("[OK]");
			// System.out.println(serviceDetail);

			return serviceDetail.toString();
		} catch (RemoteException e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);

			return null;
		}
	}

	public String getDataProductDetail(String[] BlockID) {
		GetDataProductDetailRequestDocument getDataProductDetailRequestDocument = GetDataProductDetailRequestDocument.Factory
				.newInstance();
		GetDataProductDetailRequestType getDataProductDetailRequestType = getDataProductDetailRequestDocument
				.addNewGetDataProductDetailRequest();
		DataProductIDListType dataProductIDList = getDataProductDetailRequestType
				.addNewDataProductIDList();

		for (int i = 0; i < BlockID.length; i++) {
			XmlString dataProductID = dataProductIDList.addNewDataProductID();
			dataProductID.setStringValue(BlockID[i]);
		}

		try {
			GetDataProductDetailResponseDocument getDataProductDetailResponseDocument = stub
					.getDataProductDetail(getDataProductDetailRequestDocument);
			System.out.println("[OK]");
			// System.out.println(getDataProductDetailResponseDocument);
			return getDataProductDetailResponseDocument.toString();
		} catch (RemoteException e) {
			System.out.println("[FAILED]");
			e.printStackTrace(System.err);

			return null;
		}
	}

	public static void main(String[] args) throws Exception {

		// if (args.length < 2) {
		// System.out
		// .println("Usage: KarmaAxis2Tester <KarmaServiceURL> <thread_count>");
		// }

		//String serviceURL = "http://bitternut.cs.indiana.edu:31085/axis2/services/KarmaService?wsdl";
		String serviceURL = "http://10.1.32.229:8080/axis2/services/KarmaService?wsdl";
		//String serviceURL = "http://192.168.65.136:8080/axis2/services/KarmaService";
		
		KarmaAxis2Query axis2Tester = new KarmaAxis2Query(serviceURL);
		// System.out
		// .println(axis2Tester
		// .getWorkflowGraphWithAnnotation("urn:tool:gush:peng-virtual-machine-20000-1297439073"));
		String[] ID = new String[1];
		ID[0] = "urn:tool:gush:peng-virtual-machine-20000-1297439073";
		// ID[0] = "Process_1";
		// ID[0] =
		// "urn:process-on-host:start_twister.sh:planetlab05.cs.washington.edu:20000:peng-virtual-machine-20000-1297439073";
		String id = "http://bitternut.cs.indiana.edu:33000/nam-wrf-4-278";
		System.out.println(axis2Tester.getWorkflowGraphWithoutAnnotation(id));
		// .getProvenanceHistory(id));
//				.getServiceDetailByURI(ID));
	}
}
