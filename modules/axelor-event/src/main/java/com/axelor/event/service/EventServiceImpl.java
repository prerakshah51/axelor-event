package com.axelor.event.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.ValidationException;
import org.apache.commons.io.IOUtils;

import com.axelor.apps.message.db.Message;
import com.axelor.apps.message.db.Template;
import com.axelor.apps.message.db.repo.TemplateRepository;
import com.axelor.apps.message.service.MessageService;
import com.axelor.apps.message.service.TemplateMessageService;
import com.axelor.data.Listener;
import com.axelor.data.csv.CSVImporter;
import com.axelor.db.Model;
import com.axelor.event.db.EventRegistration;
import com.axelor.event.db.repo.EventRegistrationRepository;
import com.axelor.event.db.repo.EventRepository;
import com.axelor.event.exception.IExceptionEvent;
import com.axelor.exception.AxelorException;
import com.axelor.i18n.I18n;
import com.axelor.meta.MetaFiles;
import com.axelor.meta.db.MetaFile;
import com.axelor.meta.db.MetaModel;
import com.google.common.io.Files;
import com.google.inject.Inject;
import com.axelor.event.db.Event;

public class EventServiceImpl implements EventService {

	@Inject
	EventRepository eventRepo;

	@Inject
	EventRegistrationRepository eventRegRepo;

	@Inject
	EventRegistrationServiceImpl eventRegServiceImpl;

	@Inject
	TemplateMessageService templateMessageService;

	@Inject
	MessageService messageService;

	@Inject
	TemplateRepository templateRepo;

	@Override
	public void importRegistration(MetaFile dataFile, Event event) throws IOException {
		File tempDir = Files.createTempDir();
		File csvFile = new File(tempDir, "event-reg.csv");
		Files.copy(MetaFiles.getPath(dataFile).toFile(), csvFile);
		File configXmlFile = this.getConfigXmlFile();
		CSVImporter csvImporter = new CSVImporter(configXmlFile.getAbsolutePath(), tempDir.getAbsolutePath());
		Map<String, Object> context = new HashMap<>();
		context.put("_event", event);
		csvImporter.setContext(context);
		csvImporter.addListener(new Listener() {
			@Override
			public void imported(Model bean) {
				// TODO Auto-generated method stub
			}

			@Override
			public void imported(Integer total, Integer success) {
				// TODO Auto-generated method stub
				System.out.println("Record (total): " + total);
				System.out.println("Record (success): " + success);
			}

			@Override
			public void handle(Model bean, Exception e) {
				// TODO Auto-generated method stub
				System.out.println(e.getMessage());
			}
		});
		csvImporter.run();
	}

	private File getConfigXmlFile() {
		File configFile = null;
		try {
			configFile = File.createTempFile("input-config", ".xml");
			InputStream bindFileInputStream = this.getClass()
					.getResourceAsStream("/import-configs/" + "csv-config.xml");
			if (bindFileInputStream == null) {
				throw new ValidationException(I18n.get(IExceptionEvent.IMPORT_FAIL));
			}
			FileOutputStream outputStream = new FileOutputStream(configFile);
			IOUtils.copy(bindFileInputStream, outputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configFile;
	}

	public EventRegistration setEventData(EventRegistration eventReg) {
		Event event = eventRepo.find(eventReg.getEvent().getId());
		eventReg = eventRegServiceImpl.setAmount(event, eventReg);
		// if (eventReg != null && eventReg.getRegistrationDate() != null) {
		// eventRegServiceImpl.checkEventRegistrationDate(event, eventReg);
		// }
		return eventReg;
	}

	@Override
	public void sendEmail(Event event, MetaModel metaModel) throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, AxelorException, IOException {
		Template template = templateRepo.all().filter("self.metaModel = ?", metaModel.getId()).fetchOne();
		List<EventRegistration> eventRegs = event.getEventRegistration();
		for (EventRegistration eventRegistration : eventRegs) {
			if (eventRegistration.getEmail() != null && eventRegistration.getEmailCheck() == false) {
				template.setToRecipients(eventRegistration.getEmail());
				Message message = templateMessageService.generateMessage(event, template);
				messageService.sendMessage(message);
				eventRegistration.setEmailCheck(true);
				eventRegRepo.save(eventRegistration);
			}
		}
	}
}
