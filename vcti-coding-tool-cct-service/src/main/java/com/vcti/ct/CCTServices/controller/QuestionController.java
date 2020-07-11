package com.vcti.ct.CCTServices.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.net.MediaType;
import com.vcti.ct.CCTServices.dao.QuestionDataService;
import com.vcti.ct.CCTServices.exceptions.InvalidQuestionTypeExceptoin;
import com.vcti.ct.CCTServices.model.QuesResponse;
import com.vcti.ct.CCTServices.model.Question;
import com.vcti.ct.CCTServices.model.QuestionBase;
import com.vcti.ct.CCTServices.model.QuestionTemplate;
import com.vcti.ct.CCTServices.model.Technology;
import com.vcti.ct.CCTServices.model.TechnologyMap;
import com.vcti.ct.CCTServices.model.ValidateObjQuestions;
import com.vcti.ct.CCTServices.model.ValidateSubjQuestions;

@CrossOrigin(origins = { "http://localhost:3000" })
@RestController
public class QuestionController {

	@Autowired
	private QuestionDataService questionDataService;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck() {
		return "{ \"isWorking\" : true }";
	}

	@PostMapping("/question")
	public Question addQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addQuestion(newQ);
	}

	@PostMapping("/add/obj/question")
	public Question addObjQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addObjQuestion(newQ);
	}

	@PostMapping("/add/sub/question")
	public Question addSubQuestion(@RequestBody QuestionBase newQ) {
		return questionDataService.addSubQuestion(newQ);
	}

	@PostMapping("/add/technology")
	public Technology addTechnology(@RequestBody Technology technology) {
		return questionDataService.addTechnology(technology);
	}

	@GetMapping("/technology/name/{tname}")
	public List<Technology> getTechnologyByTname(@PathVariable String tname) {
		return questionDataService.getTechnology(tname);
	}

	@GetMapping("/technology/key/{tname}")
	public List<TechnologyMap> getTechnologyByKey(@PathVariable String tname) {
		return questionDataService.getTechnologyByKey(tname);
	}

	@GetMapping("/technologies")
	public List<TechnologyMap> getAllTechnology() {
		return questionDataService.getAllTechnology();
	}

	@GetMapping("/questions")
	public List<QuestionBase> getQuestions() {
		return questionDataService.getQuestions();
	}

	@GetMapping("/questions/type/{type}/tech/{tname}")
	public List<QuestionBase> getAllQuestionsByTypeAndTname(@PathVariable String type, @PathVariable String tname) {
		return questionDataService.getAllQuestionsByTypeAndTname(type, tname);
	}

	@GetMapping("/questions/type/{type}")
	public List<QuestionBase> getAllQuestionsByType(@PathVariable String type) {
		return questionDataService.getAllQuestionsByType(type);
	}

	@GetMapping("/questions/tech/{tname}")
	public List<QuestionBase> getAllQuestionsByTname(@PathVariable String tname) {
		return questionDataService.getAllQuestionsByTname(tname);
	}

	@GetMapping("/question/{id}")
	public QuestionBase getQuestion(@PathVariable String id) {
		return questionDataService.getQuestion(id);
	}

	@GetMapping("/questionsByType/{type}")
	public List<QuestionBase> getQuestionsByType(@PathVariable String type) {
		return questionDataService.getQuestionsByType(type);
	}

	@DeleteMapping(value = "/question/{id}", produces = "application/json; charset=utf-8")
	public String deleteQuetion(@PathVariable String id) {
		return questionDataService.deleteQuestion(id);
	}

	@PutMapping("/update/sub/question/{id}")
	public Question updateSubQuestion(@RequestBody QuestionBase newQues, @PathVariable String id) {
		return questionDataService.updateSubQuestion(newQues, id);
	}

	@PutMapping("/update/obj/question/{id}")
	public Question updateObjQuestion(@RequestBody QuestionBase newQues, @PathVariable String id) {
		return questionDataService.updateObjQuestion(newQues, id);
	}

	@PutMapping("/question/{id}")
	public Question updateQuestion(@RequestBody QuestionBase newQues, @PathVariable String id) {
		return questionDataService.updateQuestions(newQues, id);
	}

	@PostMapping("/validateObjQues")
	public List<QuesResponse> validateObjQues(@RequestBody ValidateObjQuestions validateObjQ) {
		return questionDataService.validateObjQues(validateObjQ.getResponseList());
	}

	@PostMapping("/validateSubjQues")
	public QuesResponse validateSubjQues(@RequestBody ValidateSubjQuestions validateSubjQ) {
		return questionDataService.validateSubjQues(validateSubjQ);
	}

	@PostMapping("/runSubjQuesTestCode")
	public QuesResponse runSubjQuesTestCode(@RequestBody ValidateSubjQuestions validateSubjQ) {
		return questionDataService.runSubjQuesTestCode(validateSubjQ);
	}

	/**
	 * Handle QuestionTemplate
	 */

	@GetMapping("/getAllQuestionsTemplates")
	public List<QuestionTemplate> getAllQuestionsTemplates() {
		return questionDataService.getAllQuestionTemplate();
	}

	@PostMapping("/add/questionTemplate")
	public QuestionTemplate addQuestionTemplate(@RequestBody QuestionTemplate questTemplateData) {
		return questionDataService.addQuestionTemplate(questTemplateData);
	}

	@PostMapping("/add/deleteQuestionTemplate")
	public String deleteQuestionTemplate(@RequestBody String questTemplateId) {
		return questionDataService.deleteQuestionTemplate(questTemplateId);
	}

	@PostMapping(value = "uploadSubjFile")
	public ResponseEntity uploadSubjFile(@RequestParam MultipartFile file) {
		BufferedReader br = null;
		QuestionBase newQues = new QuestionBase();
		try {
			File fileToSave = new File("D:\\TEST\\" + file.getOriginalFilename());
			file.transferTo(fileToSave);
			String csvFile = "D:\\TEST\\" + file.getOriginalFilename();
			// BufferedReader br = null;
			String line = "";
			String cvsSplitBy = ",";
			br = new BufferedReader(new FileReader(csvFile));
			line = br.readLine();
			String[] arrayOfHeading = line.split(cvsSplitBy);
			String[] validate = { "type", "statement", "option1", "option2", "option3", "option4", "correctOption",
					"technology", "title", "difficulty", "experience", "topic", "expectedTime" };
			for (int i = 0; i < validate.length; i++) {
				System.out.println(validate[i]);
				System.out.println(arrayOfHeading[i]);
				String str = arrayOfHeading[i].trim();
				String str2 = validate[i].trim();
				if (str2.equalsIgnoreCase(str)) {

				} else {
					throw new InvalidQuestionTypeExceptoin("There was error in heading");
				}
			}
			while ((line = br.readLine()) != null) {

				// use comma as separator
				String[] country = line.split(cvsSplitBy);
				List al = new ArrayList();
				for (int i = 2; i < 6; i++) {
					al.add(country[i]);
				}
				newQues = new QuestionBase(country[0], country[1], al, country[6], country[7], country[8], country[9],
						country[10], country[11], country[12]);
				questionDataService.addObjQuestion(newQues);
				System.out.println("Country [code= " + country[6] + " , name=" + country[7] + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		// questionDataService.addObjQuestion(newQues);
		return ResponseEntity.ok().build();
	}

	@PostMapping(value = "uploadObjFile")
	public Question uploadObjFile(@RequestParam MultipartFile file) {
		FileInputStream fis = null;
		String[] rowHeading = { "type", "statement", "technology", "title", "difficulty", "experience", "topic",
				"expectedTime", "JunitText" };

		XSSFRow row;
		BufferedReader br = null;
		Question qt = null;
		QuestionBase newQues = new QuestionBase();
		try {
			File fileToSave = new File("D:\\TEST\\" + file.getOriginalFilename());
			file.transferTo(fileToSave);
			String csvFile = "D:\\TEST\\" + file.getOriginalFilename();
			System.out.println(csvFile);

			fis = new FileInputStream(new File(csvFile));

			XSSFWorkbook workbook = new XSSFWorkbook(fis);
			XSSFSheet spreadsheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = spreadsheet.iterator();
			row = (XSSFRow) rowIterator.next();
			Iterator<Cell> HeadingcellIterator = row.cellIterator();
			String[] validate = { "type", "statement", "technology", "title", "difficulty", "experience", "topic",
					"expectedTime", "JunitText", "methodName" };
			for (int i = 0; i < validate.length; i++) {
				System.out.println(validate[i]);
				String cell = HeadingcellIterator.next().getStringCellValue();
				System.out.println(cell);
				String str = cell.trim();
				String str2 = validate[i].trim();
				if (str2.equalsIgnoreCase(str)) {

				} else {
					throw new InvalidQuestionTypeExceptoin(
							"Enter the heading in following way:type,statement,technology,title,difficulty,ecperience,topic,expectedTime,junitText");
				}
			}
			while (rowIterator.hasNext()) {
				row = (XSSFRow) rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();

				while (cellIterator.hasNext()) {
					Cell cell1 = cellIterator.next();
					String type = cell1.getStringCellValue();
					// System.out.println(type);
					Cell cell2 = cellIterator.next();
					String statement = cell2.getStringCellValue();
					// System.out.println(statement);
					Cell cell3 = cellIterator.next();
					String technology = cell3.getStringCellValue();
					// System.out.println(technology);
					Cell cell4 = cellIterator.next();

					String title = cell4.getStringCellValue();
					// System.out.println(title);
					Cell cell5 = cellIterator.next();
					String difficulty = cell5.getStringCellValue();
					// System.out.println(difficulty);
					Cell cell6 = cellIterator.next();
					String experience = String.valueOf((int) cell6.getNumericCellValue());
					// System.out.println(experience);
					Cell cell7 = cellIterator.next();
					String topic = cell7.getStringCellValue();
					// System.out.println(topic);
					Cell cell8 = cellIterator.next();
					String expectedTime = String.valueOf((int) cell8.getNumericCellValue());
					// System.out.println(expectedTime);
					Cell cell9 = cellIterator.next();
					String junitText = cell9.getStringCellValue();
					Cell cell10 = cellIterator.next();
					String methodName = cell10.getStringCellValue();
					// System.out.println(cell10.getStringCellValue());
					newQues = new QuestionBase(type, statement, technology, title, experience, difficulty, topic,
							expectedTime, junitText, methodName);
					qt = questionDataService.addSubQuestion(newQues);

				}
				// System.out.println();
			}
			fis.close();

		} catch (Exception ex) {
			try {
				fis.close();
			} catch (Exception et) {
			}
		}

		return qt;
	}

}