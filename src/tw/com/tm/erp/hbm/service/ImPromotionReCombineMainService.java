package tw.com.tm.erp.hbm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.StringUtils;

import tw.com.tm.erp.action.SiProgramLogAction;
import tw.com.tm.erp.constants.MessageStatus;
import tw.com.tm.erp.constants.OrderStatus;
import tw.com.tm.erp.exceptions.FormException;
import tw.com.tm.erp.exceptions.NoSuchObjectException;
import tw.com.tm.erp.exceptions.ObtainSerialNoFailedException;
import tw.com.tm.erp.exceptions.ProcessFailedException;
import tw.com.tm.erp.exceptions.ValidationErrorException;
import tw.com.tm.erp.hbm.bean.BuBrand;
import tw.com.tm.erp.hbm.bean.BuCommonPhraseLine;
import tw.com.tm.erp.hbm.bean.BuEmployeeWithAddressView;
import tw.com.tm.erp.hbm.bean.BuOrderType;
import tw.com.tm.erp.hbm.bean.BuOrderTypeId;
import tw.com.tm.erp.hbm.bean.ImItemCategory;
import tw.com.tm.erp.hbm.bean.ImItemCategoryId;
import tw.com.tm.erp.hbm.bean.ImPromotion;
import tw.com.tm.erp.hbm.bean.ImPromotionCustomer;
import tw.com.tm.erp.hbm.bean.ImPromotionItem;
import tw.com.tm.erp.hbm.bean.ImPromotionReCombine;
import tw.com.tm.erp.hbm.bean.ImPromotionReCombineMod;
import tw.com.tm.erp.hbm.bean.ImPromotionShop;
import tw.com.tm.erp.hbm.dao.BaseDAO;
import tw.com.tm.erp.hbm.dao.BuBrandDAO;
import tw.com.tm.erp.hbm.dao.BuCommonPhraseLineDAO;
import tw.com.tm.erp.hbm.dao.BuEmployeeWithAddressViewDAO;
import tw.com.tm.erp.hbm.dao.ImItemCategoryDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionReCombineDAO;
import tw.com.tm.erp.hbm.dao.ImPromotionReCombineModDAO;
import tw.com.tm.erp.process.ProcessHandling;
import tw.com.tm.erp.utils.AjaxUtils;
import tw.com.tm.erp.utils.DateUtils;
import tw.com.tm.erp.utils.NumberUtils;

public class ImPromotionReCombineMainService {

	private static final Log log = LogFactory
			.getLog(ImPromotionReCombineMainService.class);
	public static final String PROGRAM_ID = "IM_PROMOTION";
	private ImPromotionDAO imPromotionDAO;
	private BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO;
	private ImPromotionItemService imPromotionItemService;
	private BuOrderTypeService buOrderTypeService;
	private SiProgramLogAction siProgramLogAction;
	private BuBrandDAO buBrandDAO;
	private BuCommonPhraseService buCommonPhraseService;
	private BuCommonPhraseLineDAO buCommonPhraseLineDAO;
	private BuEmployeeWithAddressViewService buEmployeeWithAddressViewService;
	private ImItemCategoryService imItemCategoryService;
	private ImPromotionReCombineDAO imPromotionReCombineDAO;
	private ImPromotionReCombineModDAO imPromotionReCombineModDAO;
	private ImItemCategoryDAO imItemCategoryDAO;

	public static final String[] GRID_SEARCH_FIELD_MOD_NAMES = {
			"orderTypeCode", "orderNo", "promotionCode", "promotionName",
			"inChargeName", "beginDate", "statusName", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_MOD_VALUES = { "",
			"", "", "", "", "", "", "0" };

	public static final String[] GRID_SEARCH_FIELD_NAMES = { "indexNo",
			"combineCode", "combineQuantity", "combinePrice", "itemBrand",
			"category02", "foreignCategory", "unitPrice", "enableDate",
			"endDate", "enable", "headId" };

	public static final String[] GRID_SEARCH_FIELD_DEFAULT_VALUES = { "", "",
			"", "", "", "", "", "", "", "", "", "0" };

	public static final String[] GRID_FIELD_NAMES_RECOMBINE = { "indexNo",
			"combineCode", "combineName", "combineQuantity", "combinePrice",
			"itemBrand", "itemBrandName", "reserve5", "category02",
			"category02Name", "reserve5", "foreignCategory", "unitPrice",
			"lineId", "reCombineId" };

	public static final int[] GRID_FIELD_TYPES_RECOMBINE = {
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_STRING, AjaxUtils.FIELD_TYPE_STRING,
			AjaxUtils.FIELD_TYPE_LONG, AjaxUtils.FIELD_TYPE_LONG,
			AjaxUtils.FIELD_TYPE_LONG };

	public static final String[] GRID_FIELD_DEFAULT_VALUES_RECOMBINE = { "",
			"", "", "", "", "", "", "", "", "", "", "", "", "", "" };

	public void setImPromotionDAO(ImPromotionDAO imPromotionDAO) {
		this.imPromotionDAO = imPromotionDAO;
	}

	public void setBuEmployeeWithAddressViewDAO(
			BuEmployeeWithAddressViewDAO buEmployeeWithAddressViewDAO) {
		this.buEmployeeWithAddressViewDAO = buEmployeeWithAddressViewDAO;
	}

	public void setImPromotionItemService(
			ImPromotionItemService imPromotionItemService) {
		this.imPromotionItemService = imPromotionItemService;
	}

	public void setBuOrderTypeService(BuOrderTypeService buOrderTypeService) {
		this.buOrderTypeService = buOrderTypeService;
	}

	public void setSiProgramLogAction(SiProgramLogAction siProgramLogAction) {
		this.siProgramLogAction = siProgramLogAction;
	}

	public void setBuBrandDAO(BuBrandDAO buBrandDAO) {
		this.buBrandDAO = buBrandDAO;
	}

	public void setBuCommonPhraseService(
			BuCommonPhraseService buCommonPhraseService) {
		this.buCommonPhraseService = buCommonPhraseService;
	}

	public void setBuCommonPhraseLineDAO(
			BuCommonPhraseLineDAO buCommonPhraseLineDAO) {
		this.buCommonPhraseLineDAO = buCommonPhraseLineDAO;
	}

	public void setBuEmployeeWithAddressViewService(
			BuEmployeeWithAddressViewService buEmployeeWithAddressViewService) {
		this.buEmployeeWithAddressViewService = buEmployeeWithAddressViewService;
	}

	public void setImItemCategoryService(
			ImItemCategoryService imItemCategoryService) {
		this.imItemCategoryService = imItemCategoryService;
	}

	public ImPromotion findById(Long id) {
		return imPromotionDAO.findById(id);
	}

	public void setImPromotionReCombineDAO(
			ImPromotionReCombineDAO imPromotionReCombineDAO) {
		this.imPromotionReCombineDAO = imPromotionReCombineDAO;
	}

	public void setImPromotionReCombineModDAO(
			ImPromotionReCombineModDAO imPromotionReCombineModDAO) {
		this.imPromotionReCombineModDAO = imPromotionReCombineModDAO;
	}

	public void setImItemCategoryDAO(ImItemCategoryDAO imItemCategoryDAO) {
		this.imItemCategoryDAO = imItemCategoryDAO;
	}

	public Map executeInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		try {
			HashMap argumentMap = getRequestParameter(parameterMap, false);
			Long formId = (Long) argumentMap.get("formId");
			ImPromotion form = null;
			// 建立暫存單
			if (formId == null) {
				form = createNewImPromotion(argumentMap, resultMap);
			} else {
				form = updateCurrentPromotion(argumentMap, resultMap);
			}
			resultMap.put("form", form);
			refreshImPromotion(form);
			Map multiList = new HashMap(0);
			List<BuCommonPhraseLine> allCustomerTypes = buCommonPhraseService
					.getCommonPhraseLinesById("CustomerType", false);
			List<BuCommonPhraseLine> allPriceTypes = buCommonPhraseService
					.getCommonPhraseLinesById("PriceType", false);
			List<BuOrderType> allOrderTypes = buOrderTypeService
					.findOrderbyType(form.getBrandCode(), "PM");
			if ("T2".equals(form.getBrandCode())) {
				List<BuEmployeeWithAddressView> allPurchaseAssist = null;
				List<BuEmployeeWithAddressView> allPurchaseMember = null;
				List<BuEmployeeWithAddressView> allPurchaseMaster = null;
				List<ImItemCategory> allItemCategory = null;

				// if(form.getOrderTypeCode().equals("PMC")){
				// log.info("enable="+argumentMap.get("enable")+"----beginDate="+argumentMap.get("beginDate"));
				// form.setEnable((String)argumentMap.get("enable"));
				// form.setBeginDate((Date)argumentMap.get("beginDate"));
				// }

				if (OrderStatus.SAVE.equals(form.getStatus())
						|| OrderStatus.REJECT.equals(form.getStatus())) {
					allItemCategory = imItemCategoryService.findByCategoryType(
							form.getBrandCode(), "ITEM_CATEGORY");
					if (allItemCategory != null && allItemCategory.size() > 0) {
						String itemCategory = null;
						if (form.getItemCategory() == null) {
							itemCategory = allItemCategory.get(0).getId()
									.getCategoryCode();
						} else {
							itemCategory = form.getItemCategory();
						}
						HashMap mapResult = getPurchaseEmployee(itemCategory);
						allPurchaseAssist = (List<BuEmployeeWithAddressView>) mapResult
								.get("purchaseAssist");
						allPurchaseMember = (List<BuEmployeeWithAddressView>) mapResult
								.get("purchaseMember");
						allPurchaseMaster = (List<BuEmployeeWithAddressView>) mapResult
								.get("purchaseMaster");
						if (form.getItemCategory() == null) {
							form.setItemCategory(itemCategory);
							// ================預設採購助理=KEY單人員==========================
							for (BuEmployeeWithAddressView buEmployee : allPurchaseAssist) {
								if (buEmployee.getEmployeeCode().equals(
										form.getCreatedBy())) {
									form.setPurchaseAssist(buEmployee
											.getEmployeeCode());
								}
							}
							// ================預設採購人員=KEY單人員==========================
							for (BuEmployeeWithAddressView buEmployee : allPurchaseMember) {
								if (buEmployee.getEmployeeCode().equals(
										form.getCreatedBy())) {
									form.setPurchaseMember(buEmployee
											.getEmployeeCode());
								}
							}
							// =================預設採購主管=KEY單人員===========================
							for (BuEmployeeWithAddressView buEmployee : allPurchaseMaster) {
								if (buEmployee.getEmployeeCode().equals(
										form.getCreatedBy())) {
									form.setPurchaseMaster(buEmployee
											.getEmployeeCode());
								}
							}
						}
						multiList.put("allItemCategory", AjaxUtils
								.produceSelectorData(allItemCategory,
										"categoryCode", "categoryName", true,
										true));
						multiList.put("allPurchaseAssist", AjaxUtils
								.produceSelectorData(allPurchaseAssist,
										"employeeCode", "chineseName", true,
										true));
						multiList.put("allPurchaseMember", AjaxUtils
								.produceSelectorData(allPurchaseMember,
										"employeeCode", "chineseName", true,
										true));
						multiList.put("allPurchaseMaster", AjaxUtils
								.produceSelectorData(allPurchaseMaster,
										"employeeCode", "chineseName", true,
										true));
					} else {
						throw new ValidationErrorException("查無"
								+ form.getBrandCode() + "的業種子類！");
					}
				} else {
					// ==================================業種子類=======================================
					allItemCategory = new ArrayList(0);
					ImItemCategory itemCategoryPO = imItemCategoryService
							.findById(form.getBrandCode(), "ITEM_CATEGORY",
									form.getItemCategory());
					if (itemCategoryPO != null) {
						allItemCategory.add(itemCategoryPO);
						multiList.put("allItemCategory", AjaxUtils
								.produceSelectorData(allItemCategory,
										"categoryCode", "categoryName", true,
										true));
					} else {
						ImItemCategoryId itemCategoryId = new ImItemCategoryId();
						itemCategoryId.setCategoryCode(form.getItemCategory());
						ImItemCategory itemCategory = new ImItemCategory();
						itemCategory.setId(itemCategoryId);
						allItemCategory.add(itemCategory);
						multiList.put("allItemCategory", AjaxUtils
								.produceSelectorData(allItemCategory,
										"categoryCode", "categoryCode", false,
										true));
					}
					// ==================================採購助理=======================================
					allPurchaseAssist = new ArrayList(0);
					BuEmployeeWithAddressView purchaseAssistView = buEmployeeWithAddressViewDAO
							.findbyBrandCodeAndEmployeeCode(
									form.getBrandCode(), form
											.getPurchaseAssist());
					if (purchaseAssistView != null) {
						allPurchaseAssist.add(purchaseAssistView);
						multiList.put("allPurchaseAssist", AjaxUtils
								.produceSelectorData(allPurchaseAssist,
										"employeeCode", "chineseName", true,
										true));
					} else {
						purchaseAssistView = new BuEmployeeWithAddressView();
						purchaseAssistView.setEmployeeCode(form
								.getPurchaseAssist());
						allPurchaseAssist.add(purchaseAssistView);
						multiList.put("allPurchaseAssist", AjaxUtils
								.produceSelectorData(allPurchaseAssist,
										"employeeCode", "employeeCode", false,
										true));
					}
					// ==================================採購人員=======================================
					allPurchaseMember = new ArrayList(0);
					BuEmployeeWithAddressView purchaseMemberView = buEmployeeWithAddressViewDAO
							.findbyBrandCodeAndEmployeeCode(
									form.getBrandCode(), form
											.getPurchaseMember());
					if (purchaseMemberView != null) {
						allPurchaseMember.add(purchaseMemberView);
						multiList.put("allPurchaseMember", AjaxUtils
								.produceSelectorData(allPurchaseMember,
										"employeeCode", "chineseName", true,
										true));
					} else {
						purchaseMemberView = new BuEmployeeWithAddressView();
						purchaseMemberView.setEmployeeCode(form
								.getPurchaseMember());
						allPurchaseMember.add(purchaseMemberView);
						multiList.put("allPurchaseMember", AjaxUtils
								.produceSelectorData(allPurchaseMember,
										"employeeCode", "employeeCode", false,
										true));
					}
					// ==================================採購主管=======================================
					allPurchaseMaster = new ArrayList(0);
					BuEmployeeWithAddressView purchaseMasterView = buEmployeeWithAddressViewDAO
							.findbyBrandCodeAndEmployeeCode(
									form.getBrandCode(), form
											.getPurchaseMaster());
					if (purchaseMasterView != null) {
						allPurchaseMaster.add(purchaseMasterView);
						multiList.put("allPurchaseMaster", AjaxUtils
								.produceSelectorData(allPurchaseMaster,
										"employeeCode", "chineseName", true,
										true));
					} else {
						purchaseMasterView = new BuEmployeeWithAddressView();
						purchaseMasterView.setEmployeeCode(form
								.getPurchaseMaster());
						allPurchaseMaster.add(purchaseMasterView);
						multiList.put("allPurchaseMaster", AjaxUtils
								.produceSelectorData(allPurchaseMaster,
										"employeeCode", "employeeCode", false,
										true));
					}
				}
			} else {

				List<ImItemCategory> allItemCategory = null;
				if (OrderStatus.SAVE.equals(form.getStatus())
						|| OrderStatus.REJECT.equals(form.getStatus())) {
					allItemCategory = imItemCategoryService.findByCategoryType(
							form.getBrandCode(), "ITEM_CATEGORY");
					if (allItemCategory != null && allItemCategory.size() > 0) {
						String itemCategory = null;
						if (form.getItemCategory() == null) {
							itemCategory = allItemCategory.get(0).getId()
									.getCategoryCode();
						} else {
							itemCategory = form.getItemCategory();
						}

						if (form.getItemCategory() == null) {
							form.setItemCategory(itemCategory);
						}
						multiList.put("allItemCategory", AjaxUtils
								.produceSelectorData(allItemCategory,
										"categoryCode", "categoryName", true,
										true));

					} else {
						throw new ValidationErrorException("查無"
								+ form.getBrandCode() + "的業種！");
					}
				} else {
					// ==================================業種子類=======================================
					allItemCategory = new ArrayList(0);
					ImItemCategory itemCategoryPO = imItemCategoryService
							.findById(form.getBrandCode(), "ITEM_CATEGORY",
									form.getItemCategory());
					if (itemCategoryPO != null) {
						allItemCategory.add(itemCategoryPO);
						multiList.put("allItemCategory", AjaxUtils
								.produceSelectorData(allItemCategory,
										"categoryCode", "categoryName", true,
										true));
					} else {
						ImItemCategoryId itemCategoryId = new ImItemCategoryId();
						itemCategoryId.setCategoryCode(form.getItemCategory());
						ImItemCategory itemCategory = new ImItemCategory();
						itemCategory.setId(itemCategoryId);
						allItemCategory.add(itemCategory);
						multiList.put("allItemCategory", AjaxUtils
								.produceSelectorData(allItemCategory,
										"categoryCode", "categoryCode", false,
										true));
					}
				}
			}

			multiList.put("allCustomerTypes", AjaxUtils.produceSelectorData(
					allCustomerTypes, "lineCode", "name", false, false));
			multiList.put("allPriceTypes", AjaxUtils.produceSelectorData(
					allPriceTypes, "lineCode", "name", false, false));
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(
					allOrderTypes, "orderTypeCode", "name", true, false));
			resultMap.put("multiList", multiList);

			return resultMap;
		} catch (Exception ex) {
			log.error("促銷單初始化失敗，原因：" + ex.toString());
			throw new Exception("促銷單初始化失敗，原因：" + ex.toString());
		}
	}

	private HashMap getRequestParameter(Map parameterMap, boolean isSubmitAction)
			throws Exception {

		Object otherBean = parameterMap.get("vatBeanOther");
		String loginBrandCode = (String) PropertyUtils.getProperty(otherBean,
				"loginBrandCode");
		String loginEmployeeCode = (String) PropertyUtils.getProperty(
				otherBean, "loginEmployeeCode");
		String orderTypeCode = (String) PropertyUtils.getProperty(otherBean,
				"orderTypeCode");
		String formIdString = (String) PropertyUtils.getProperty(otherBean,
				"formId");
		Long formId = StringUtils.hasText(formIdString) ? Long
				.valueOf(formIdString) : null;
		HashMap conditionMap = new HashMap();
		conditionMap.put("loginBrandCode", loginBrandCode);
		conditionMap.put("loginEmployeeCode", loginEmployeeCode);
		conditionMap.put("orderTypeCode", orderTypeCode);
		conditionMap.put("formId", formId);
		if (isSubmitAction) {
			String beforeChangeStatus = (String) PropertyUtils.getProperty(
					otherBean, "beforeChangeStatus");
			String formStatus = (String) PropertyUtils.getProperty(otherBean,
					"formStatus");
			conditionMap.put("beforeChangeStatus", beforeChangeStatus);
			conditionMap.put("formStatus", formStatus);
		}

		return conditionMap;
	}

	public ImPromotion createNewImPromotion(Map argumentMap, Map resultMap)
			throws Exception {

		try {
			String loginBrandCode = (String) argumentMap.get("loginBrandCode");
			String loginEmployeeCode = (String) argumentMap
					.get("loginEmployeeCode");
			String orderTypeCode = (String) argumentMap.get("orderTypeCode");
			BuOrderTypeId orderTypeId = new BuOrderTypeId(loginBrandCode,
					orderTypeCode);
			BuOrderType orderTypePO = buOrderTypeService.findById(orderTypeId);
			if (orderTypePO == null) {
				throw new NoSuchObjectException("查無品牌(" + loginBrandCode
						+ ")、單別(" + orderTypeCode + ")的單別資料！");
			}
			// 將此品牌的VIPTYPE塞入此促銷單
			List imPromotionCustomers = new ArrayList(0);
			// getVipTypeInfomation(imPromotionCustomers, loginBrandCode, true);
			ImPromotion form = new ImPromotion();
			form.setBrandCode(loginBrandCode);
			form.setOrderTypeCode(orderTypeCode);
			form.setPriceType(orderTypePO.getPriceType());
			form.setStatus(OrderStatus.SAVE);
			form.setInCharge(loginEmployeeCode);
			form.setIsAllItem("N");
			form.setIsAllShop("N");
			form.setIsAllCustomer("N");
			form.setCreatedBy(loginEmployeeCode);
			form.setLastUpdatedBy(loginEmployeeCode);
			form.setImPromotionCustomers(imPromotionCustomers);
			saveTmp(form);

			return form;
		} catch (Exception ex) {
			log.error("產生新促銷單失敗，原因：" + ex.toString());
			throw new Exception("產生新促銷單發生錯誤！");
		}
	}

	public ImPromotion updateCurrentPromotion(Map argumentMap, Map resultMap)
			throws FormException, Exception {

		try {
			Long formId = (Long) argumentMap.get("formId");
			// String loginBrandCode = (String)
			// argumentMap.get("loginBrandCode");
			ImPromotion form = findById(formId);
			if (form != null) {
				if (OrderStatus.SAVE.equals(form.getStatus())
						|| OrderStatus.REJECT.equals(form.getStatus())) {
					// getVipTypeInfomation(form.getImPromotionCustomers(),
					// form.getBrandCode(), false);
					imPromotionDAO.update(form);
				}
				return form;
			} else {
				throw new NoSuchObjectException("查無促銷單主鍵(" + formId + ")的資料！");
			}
		} catch (FormException fe) {
			log.error("查詢促銷單失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("查詢促銷單發生錯誤，原因：" + ex.toString());
			throw new Exception("查詢促銷單發生錯誤！");
		}
	}

	private void refreshImPromotion(ImPromotion form) {

		String brandCode = form.getBrandCode();
		String brandName = "";
		BuBrand buBrandPO = buBrandDAO.findById(brandCode);
		if (buBrandPO == null) {
			brandName = "查無此品牌資料";
		} else {
			brandName = buBrandPO.getBrandName();
		}
		form.setBrandName(brandName);

		// 負責人員
		String inChargeName = "";
		if (StringUtils.hasText(form.getInCharge())) {
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
					.findbyBrandCodeAndEmployeeCode(brandCode, form
							.getInCharge());
			if (employeeWithAddressView == null) {
				inChargeName = "查無此員工資料";
			} else {
				inChargeName = employeeWithAddressView.getChineseName();
			}
		}
		form.setInChargeName(inChargeName);
		// 填單人員
		String createdByName = "";
		if (StringUtils.hasText(form.getCreatedBy())) {
			BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
					.findbyBrandCodeAndEmployeeCode(brandCode, form
							.getCreatedBy());
			if (employeeWithAddressView == null) {
				createdByName = "查無此員工資料";
			} else {
				createdByName = employeeWithAddressView.getChineseName();
			}
		}
		form.setCreatedByName(createdByName);
		// 狀態
		form.setStatusName(OrderStatus.getChineseWord(form.getStatus()));
	}

	/**
	 * 取得品牌的VIPTYPE並塞入此促銷單
	 * 
	 * @param imPromotionCustomers
	 * @param brandCode
	 * @param isNew
	 */
	private void getVipTypeInfomation(
			List<ImPromotionCustomer> imPromotionCustomers, String brandCode,
			boolean isNew) {

		List<BuCommonPhraseLine> allVIPType = buCommonPhraseService
				.findCommonPhraseLineByAttribute("VIPType",
						"model.attribute1='" + brandCode + "'");
		if (allVIPType != null && allVIPType.size() > 0) {
			HashMap vipMap = new HashMap();
			for (BuCommonPhraseLine vipType : allVIPType) {
				String vipTypeCode = vipType.getId().getLineCode();
				vipMap.put(vipTypeCode, vipTypeCode);
				if (isNew) {
					ImPromotionCustomer promotionCustomer = new ImPromotionCustomer();
					promotionCustomer.setVipTypeCode(vipTypeCode);
					promotionCustomer.setEnable("N");
					imPromotionCustomers.add(promotionCustomer);
				}
			}
			if (!isNew) {
				for (ImPromotionCustomer promotionCustomer : imPromotionCustomers) {
					if (vipMap.get(promotionCustomer.getVipTypeCode()) != null) {
						vipMap.remove(promotionCustomer.getVipTypeCode());
					}
				}
				Set remnantSet = vipMap.entrySet();
				Map.Entry[] remnantVipTypeEntry = (Map.Entry[]) remnantSet
						.toArray(new Map.Entry[remnantSet.size()]);
				for (Map.Entry entry : remnantVipTypeEntry) {
					String vipTypeCode = (String) entry.getValue();
					ImPromotionCustomer promotionCustomer = new ImPromotionCustomer();
					promotionCustomer.setVipTypeCode(vipTypeCode);
					promotionCustomer.setEnable("N");
					imPromotionCustomers.add(promotionCustomer);
				}
			}
		}
	}

	private HashMap getPurchaseEmployee(String itemCategory) {

		List<BuEmployeeWithAddressView> purchaseAssist = null;
		List<BuEmployeeWithAddressView> purchaseMember = null;
		List<BuEmployeeWithAddressView> purchaseMaster = null;
		HashMap mapParameter = new HashMap();
		if (StringUtils.hasText(itemCategory)) {
			mapParameter.put("itemCategory", itemCategory);
			mapParameter.put("employeeDepartment", "506");
			// 採購助理
			mapParameter.put("employeePosition", "A");
			purchaseAssist = buEmployeeWithAddressViewService
					.findByBuItemCategoryPrivilege(mapParameter);
			// 採購人員
			mapParameter.put("employeePosition", "P");
			purchaseMember = buEmployeeWithAddressViewService
					.findByBuItemCategoryPrivilege(mapParameter);
			// 採購主管
			mapParameter.put("employeePosition", "M");
			purchaseMaster = buEmployeeWithAddressViewService
					.findByBuItemCategoryPrivilege(mapParameter);
		}

		if (purchaseAssist == null || purchaseAssist.size() == 0) {
			purchaseAssist = new ArrayList(0);
		}
		if (purchaseMember == null || purchaseMember.size() == 0) {
			purchaseMember = new ArrayList(0);
		}
		if (purchaseMaster == null || purchaseMaster.size() == 0) {
			purchaseMaster = new ArrayList(0);
		}
		mapParameter.put("purchaseAssist", purchaseAssist);
		mapParameter.put("purchaseMember", purchaseMember);
		mapParameter.put("purchaseMaster", purchaseMaster);

		return mapParameter;
	}

	/**
	 * 取得暫時單號存檔
	 * 
	 * @param promotion
	 * @throws Exception
	 */
	public void saveTmp(ImPromotion promotion) throws Exception {

		try {
			String tmpOrderNo = AjaxUtils.getTmpOrderNo();
			promotion.setOrderNo(tmpOrderNo);
			promotion.setLastUpdateDate(new Date());
			promotion.setCreationDate(new Date());
			imPromotionDAO.save(promotion);
		} catch (Exception ex) {
			log.error("取得暫時單號儲存促銷單發生錯誤，原因：" + ex.toString());
			throw new Exception("取得暫時單號儲存促銷單發生錯誤，原因：" + ex.getMessage());
		}
	}

	public Long getPromotionHeadId(Object bean) throws FormException, Exception {

		Long headId = null;
		String id = (String) PropertyUtils.getProperty(bean, "headId");
		if (StringUtils.hasText(id)) {
			headId = NumberUtils.getLong(id);
		} else {
			throw new ValidationErrorException("傳入的促銷單主鍵為空值！");
		}

		return headId;
	}

	/**
	 * 更新促銷單主檔及明細檔
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map updateAJAXPromotion(Map parameterMap) throws FormException,
			Exception {

		HashMap resultMap = new HashMap();
		try {
			Object formBindBean = parameterMap.get("vatBeanFormBind");
			Object formLinkBean = parameterMap.get("vatBeanFormLink");
			Object otherBean = parameterMap.get("vatBeanOther");
			Long headId = getPromotionHeadId(formLinkBean);
			String formStatus = (String) PropertyUtils.getProperty(otherBean,
					"formStatus");
			String beforeChangeStatus = (String) PropertyUtils.getProperty(
					otherBean, "beforeChangeStatus");
			String employeeCode = (String) PropertyUtils.getProperty(otherBean,
					"loginEmployeeCode");

			// 取得欲更新的bean
			ImPromotion promotionPO = getActualPromotion(headId);
			if (OrderStatus.SAVE.equals(formStatus)) {
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
			} else if ((OrderStatus.SAVE.equals(beforeChangeStatus) || OrderStatus.REJECT
					.equals(beforeChangeStatus))
					&& OrderStatus.SIGNING.equals(formStatus)) {
				// ========================檢核資料======================================
				String identification = MessageStatus.getIdentification(
						promotionPO.getBrandCode(), promotionPO
								.getOrderTypeCode(), promotionPO.getOrderNo());
				AjaxUtils.copyJSONBeantoPojoBean(formBindBean, promotionPO);
				// imPromotionItemService.refreshFieldData(promotionPO);
				promotionPO.setLastUpdatedBy(employeeCode);
				siProgramLogAction.deleteProgramLog(PROGRAM_ID, null,
						identification);
				// imPromotionItemService.checkPromotionData(promotionPO,
				// PROGRAM_ID, identification); //暫時註解Steve
				// ============remove delete mark record=============
				removeDeleteMarkLineForItem(promotionPO);
				removeDeleteMarkLineForShop(promotionPO);
			}
			promotionPO.setStatus(formStatus);
			String resultMsg = modifyImPromotion(promotionPO, employeeCode);
			resultMap.put("entityBean", promotionPO);
			resultMap.put("resultMsg", resultMsg);

			return resultMap;
		} catch (FormException fe) {
			log.error("促銷單存檔失敗，原因：" + fe.toString());
			throw new FormException(fe.getMessage());
		} catch (Exception ex) {
			log.error("促銷單存檔時發生錯誤，原因：" + ex.toString());
			throw new Exception("促銷單存檔時發生錯誤，原因：" + ex.getMessage());
		}
	}

	/**
	 * remove delete mark record(item)
	 * 
	 * @param promotion
	 */
	private void removeDeleteMarkLineForItem(ImPromotion promotion) {

		List promotionItems = promotion.getImPromotionItems();
		if (promotionItems != null && promotionItems.size() > 0) {
			for (int i = promotionItems.size() - 1; i >= 0; i--) {
				ImPromotionItem promotionItem = (ImPromotionItem) promotionItems
						.get(i);
				if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(promotionItem
						.getIsDeleteRecord())) {
					promotionItems.remove(promotionItem);
				}
			}
		}
	}

	public ImPromotion getActualPromotion(Long headId) throws FormException,
			Exception {

		ImPromotion promotionPO = findById(headId);
		if (promotionPO == null) {
			throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
		}

		return promotionPO;
	}

	/**
	 * remove delete mark record(shop)
	 * 
	 * @param promotion
	 */
	private void removeDeleteMarkLineForShop(ImPromotion promotion) {

		List promotionShops = promotion.getImPromotionShops();
		if (promotionShops != null && promotionShops.size() > 0) {
			for (int i = promotionShops.size() - 1; i >= 0; i--) {
				ImPromotionShop promotionShop = (ImPromotionShop) promotionShops
						.get(i);
				if (AjaxUtils.IS_DELETE_RECORD_TRUE.equals(promotionShop
						.getIsDeleteRecord())) {
					promotionShops.remove(promotionShop);
				}
			}
		}
	}

	/**
	 * 暫存單號取實際單號並更新至Promotion主檔及明細檔
	 * 
	 * @param promotion
	 * @param loginUser
	 * @return String
	 * @throws ObtainSerialNoFailedException
	 * @throws FormException
	 * @throws Exception
	 */
	private String modifyImPromotion(ImPromotion promotion, String loginUser)
			throws ObtainSerialNoFailedException, FormException, Exception {

		if (AjaxUtils.isTmpOrderNo(promotion.getOrderNo())) {
			String serialNo = buOrderTypeService.getOrderSerialNo(promotion
					.getBrandCode(), promotion.getOrderTypeCode());
			if (!serialNo.equals("unknow")) {
				promotion.setOrderNo(serialNo);
			} else {
				throw new ObtainSerialNoFailedException("取得"
						+ promotion.getOrderTypeCode() + "單號失敗！");
			}
		}
		promotion.setLastUpdatedBy(loginUser);
		promotion.setLastUpdateDate(new Date());
		imPromotionDAO.update(promotion);
		return promotion.getOrderTypeCode() + "-" + promotion.getOrderNo()
				+ "存檔成功！";
	}

	public static Object[] startProcess(ImPromotion form)
			throws ProcessFailedException {

		try {
			String packageId = "Im_Promotion";
			String processId = "reCombine";
			String version = "20141218";
			String sourceReferenceType = "ImPromotion (T2)";

			HashMap context = new HashMap();
			context.put("brandCode", form.getBrandCode());
			context.put("formId", form.getHeadId());
			context.put("orderType", form.getOrderTypeCode());
			context.put("orderNo", form.getOrderNo());
			return ProcessHandling.start(packageId, processId, version,
					sourceReferenceType, context);
		} catch (Exception ex) {
			log.error("促銷流程啟動失敗，原因：" + ex.toString());
			throw new ProcessFailedException("促銷流程啟動失敗！");
		}
	}

	public static Object[] completeAssignment(long assignmentId,
			boolean approveResult) throws ProcessFailedException {

		try {
			HashMap context = new HashMap();
			context.put("approveResult", approveResult);

			return ProcessHandling.completeAssignment(assignmentId, context);
		} catch (Exception ex) {
			log.error("完成促銷工作任務失敗，原因：" + ex.toString());
			throw new ProcessFailedException("完成促銷工作任務失敗！");
		}
	}

	public List<Properties> getAJAXPageDataForReCombine(Properties httpRequest)
			throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			Long headId = NumberUtils
					.getLong(httpRequest.getProperty("headId"));// 要顯示的HEAD_ID
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入Head的值=========================
			String brandCode = httpRequest.getProperty("brandCode");// 品牌代號
			String formStatus = httpRequest.getProperty("formStatus");// 狀態
			HashMap map = new HashMap();
			map.put("brandCode", brandCode);
			map.put("formStatus", formStatus);
			// ======================取得頁面所需資料===========================
			List<ImPromotionReCombineMod> imPromotionReCombines = imPromotionReCombineModDAO
					.findPageLine(headId, iSPage, iPSize);

			if (imPromotionReCombines != null
					&& imPromotionReCombines.size() > 0) {
				this.setLineOtherColumn(imPromotionReCombines, brandCode);
				// 取得第一筆的INDEX
				Long firstIndex = imPromotionReCombines.get(0).getIndexNo();
				// 取得最後一筆 INDEX
				Long maxIndex = imPromotionReCombineModDAO
						.findPageLineMaxIndex(headId);
				// refreshShopData(map, imPromotionReCombines);
				result
						.add(AjaxUtils.getAJAXPageData(httpRequest,
								GRID_FIELD_NAMES_RECOMBINE,
								GRID_FIELD_DEFAULT_VALUES_RECOMBINE,
								imPromotionReCombines, gridDatas, firstIndex,
								maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_FIELD_NAMES_RECOMBINE,
						GRID_FIELD_DEFAULT_VALUES_RECOMBINE, gridDatas));
			}
			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的促銷專櫃明細發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的促銷專櫃明細失敗！");
		}
	}

	/**
	 * 更新明細
	 * 
	 * @param httpRequest
	 * @return
	 * @throws Exception
	 */
	public List<Properties> updateOrSaveCombinePageLinesData(
			Properties httpRequest) throws Exception {

		try {
			ImPromotion promotion = null;
			Date date = new Date();
			Long headId = NumberUtils
					.getLong(httpRequest.getProperty("headId"));
			String loginUser = httpRequest.getProperty("loginEmployeeCode");
			String gridData = httpRequest.getProperty(AjaxUtils.GRID_DATA);
			int gridLineFirstIndex = NumberUtils.getInt(httpRequest
					.getProperty(AjaxUtils.GRID_LINE_FIRST_INDEX));
			int gridRowCount = NumberUtils.getInt(httpRequest
					.getProperty(AjaxUtils.GRID_ROW_COUNT));

			promotion = findById(headId);
			List<Properties> upRecords = AjaxUtils.getGridFieldValue(gridData,
					gridLineFirstIndex, gridRowCount,
					GRID_FIELD_NAMES_RECOMBINE);

			int indexNo = imPromotionReCombineModDAO.findPageLineMaxIndex(
					headId).intValue();
			if (upRecords != null) {
				for (Properties upRecord : upRecords) {
					Long lineId = NumberUtils.getLong(upRecord
							.getProperty("lineId"));
					String combineCode = upRecord
							.getProperty(GRID_FIELD_NAMES_RECOMBINE[1]);
					if (StringUtils.hasText(combineCode)) {
						ImPromotionReCombineMod promotionReCombineMod = imPromotionReCombineModDAO
								.findReCombineByIdentification(headId, lineId);
						if (promotionReCombineMod != null) {
							AjaxUtils.setPojoProperties(promotionReCombineMod,
									upRecord, GRID_FIELD_NAMES_RECOMBINE,
									GRID_FIELD_TYPES_RECOMBINE);
							promotionReCombineMod.setLastUpdateDate(new Date());
							promotionReCombineMod.setLastUpdatedBy(loginUser);
							imPromotionReCombineModDAO
									.update(promotionReCombineMod);
						} else {
							indexNo++;
							ImPromotionReCombineMod ReCombine = new ImPromotionReCombineMod();
							AjaxUtils.setPojoProperties(ReCombine, upRecord,
									GRID_FIELD_NAMES_RECOMBINE,
									GRID_FIELD_TYPES_RECOMBINE);
							ReCombine.setIndexNo(Long.valueOf(indexNo));
							ReCombine.setCreatedBy(loginUser);
							ReCombine.setLastUpdatedBy(loginUser);
							ReCombine.setCreationDate(new Date());
							ReCombine.setLastUpdateDate(new Date());
							ReCombine.setImPromotion(promotion);
							imPromotionReCombineModDAO.save(ReCombine);
						}
					}
				}
			}
			return new ArrayList();
		} catch (Exception ex) {
			log.error("更新組合單明細時發生錯誤，原因：" + ex.toString());
			throw new Exception("更新組合單明細失敗！" + ex.getMessage());
		}
	}

	private void setLineOtherColumn(
			List<ImPromotionReCombineMod> imPromotionReCombines,
			String brandCode) throws Exception {
		for (ImPromotionReCombineMod imPromotionReCombineMod : imPromotionReCombines) {
			String categoryType = "";
			String categoryCode = "";
			String categoryName = "";
			if (StringUtils.hasText(imPromotionReCombineMod.getItemBrand())) {
				categoryType = "ItemBrand";
				categoryCode = imPromotionReCombineMod.getItemBrand();
				ImItemCategory category = imItemCategoryDAO.findByCategoryCode(
						brandCode, categoryType, categoryCode, "Y");
				;
				if (category != null) {
					categoryName = category.getCategoryName();
				} else {
					categoryName = "查無此類別";
				}
				imPromotionReCombineMod.setItemBrandName(categoryName);
			} else {
				imPromotionReCombineMod.setItemBrandName("");
			}

			if (StringUtils.hasText(imPromotionReCombineMod.getCategory02())) {
				categoryType = "CATEGORY02";
				categoryCode = imPromotionReCombineMod.getCategory02();
				ImItemCategory category = imItemCategoryDAO.findByCategoryCode(
						brandCode, categoryType, categoryCode, "Y");
				;
				if (category != null) {
					categoryName = category.getCategoryName();
				} else {
					categoryName = "查無此類別";
				}
				imPromotionReCombineMod.setCategory02Name(categoryName);
			} else {
				imPromotionReCombineMod.setCategory02Name("");
			}
		}
	}

	public void updatePromotionReCombine(ImPromotion imPromotion)
			throws Exception {
		List<ImPromotionReCombineMod> imPromotionReCombines = imPromotion.getImPromotionReCombines();
		List<ImPromotionReCombine> imReCombines = null;
		if (imPromotionReCombines != null && imPromotionReCombines.size() > 0) {
			for (ImPromotionReCombineMod imPromotionReCombineMod : imPromotionReCombines) {
				if (imPromotion.getEnable().equals("Y")) {
					ImPromotionReCombine reCombine = new ImPromotionReCombine();
					reCombine.setCombineCode(imPromotionReCombineMod.getCombineCode());
					reCombine.setCombineName(imPromotionReCombineMod.getCombineName());
					reCombine.setCombinePrice(imPromotionReCombineMod.getCombinePrice());
					reCombine.setCombineQuantity(imPromotionReCombineMod.getCombineQuantity());
					reCombine.setCategory02(imPromotionReCombineMod.getCategory02());
					reCombine.setItemBrand(imPromotionReCombineMod.getItemBrand());
					reCombine.setForeignCategory(imPromotionReCombineMod.getForeignCategory());
					reCombine.setUnitPrice(imPromotionReCombineMod.getUnitPrice());
					reCombine.setCreatedBy(imPromotionReCombineMod.getCreatedBy());
					reCombine.setCreationDate(new Date());
					reCombine.setLastUpdatedBy(imPromotionReCombineMod.getLastUpdatedBy());
					reCombine.setLastUpdateDate(new Date());
					reCombine.setEnable("Y");
					reCombine.setEnableDate(imPromotion.getBeginDate());
					imPromotionReCombineDAO.save(reCombine);
				} else {
					if (imPromotionReCombineMod.getReCombineId() != null) {
						imReCombines = imPromotionReCombineDAO.findReCombineByIdentification(imPromotionReCombineMod.getReCombineId());
						if (imReCombines != null && imReCombines.size() > 0) {
							//imReCombines.get(0).setEnable("N");
							imReCombines.get(0).setEndDate(imPromotion.getBeginDate());
							imReCombines.get(0).setLastUpdatedBy(imPromotionReCombineMod.getLastUpdatedBy());
							imReCombines.get(0).setLastUpdateDate(new Date());
							imPromotionReCombineDAO.update(imReCombines.get(0));
						}
					}
				}
			}
		} else {
			throw new Exception("無組合單明細更新！");
		}
	}

	/**
	 * 顯示促銷活動查詢頁面的line
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchPageData(Properties httpRequest)
			throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入查詢的值=========================
			String brandCode = httpRequest.getProperty("brandCode");
			String orderTypeCode = httpRequest.getProperty("orderTypeCode");
			String inCharge = httpRequest.getProperty("inCharge");
			String status = httpRequest.getProperty("status");
			String combineCode = httpRequest.getProperty("combineCode");
			String startOrderNo = httpRequest.getProperty("startOrderNo");
			String endOrderNo = httpRequest.getProperty("endOrderNo");
			String startDate = httpRequest.getProperty("startDate");
			// String combineName = httpRequest.getProperty("combineName");
			// 促銷增加搜尋欄位(wade)
			String itemBrand = httpRequest.getProperty("itemBrand");
			String category02 = httpRequest.getProperty("category02");
			String foreignCategory = httpRequest.getProperty("foreignCategory");

			Date actualStartDate = null;
			if (StringUtils.hasText(startDate)) {
				actualStartDate = DateUtils.parseDate(
						DateUtils.C_DATE_PATTON_SLASH, startDate);
			}
			// =======================執行查詢==============================
			HashMap findObjs = new HashMap();
			findObjs.put("brandCode", brandCode);
			findObjs.put("orderTypeCode", orderTypeCode);
			findObjs.put("inCharge", inCharge);
			findObjs.put("status", status);
			findObjs.put("combineCode", combineCode);
			findObjs.put("startOrderNo", startOrderNo);
			findObjs.put("endOrderNo", endOrderNo);
			findObjs.put("startDate", actualStartDate);
			// findObjs.put("combineName", combineName);
			// 促銷增加搜尋欄位(wade)
			findObjs.put("itemBrand", itemBrand);
			findObjs.put("category02", category02);
			findObjs.put("foreignCategory", foreignCategory);

			HashMap promotionMap = imPromotionDAO.findPageLine(findObjs,
					iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<ImPromotion> promotions = (List<ImPromotion>) promotionMap
					.get(BaseDAO.TABLE_LIST);
			System.out.println("ResultSize=[" + promotions.size() + "]");
			if (promotions != null && promotions.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				promotionMap = imPromotionDAO.findPageLine(findObjs, -1,
						iPSize, BaseDAO.QUERY_RECORD_COUNT);
				Long maxIndex = (Long) promotionMap
						.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				refreshSearchResultBeans(promotions, findObjs);
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_MOD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_MOD_VALUES, promotions,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_MOD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_MOD_VALUES, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的促銷活動查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的促銷活動查詢失敗！");
		}
	}

	/**
	 * 查詢明細額外撈取欄位
	 * 
	 * @param searchResult
	 * @param findObjs
	 */
	private void refreshSearchResultBeans(List searchResult, HashMap findObjs) {

		if (searchResult != null && searchResult.size() > 0) {
			for (int i = 0; i < searchResult.size(); i++) {
				ImPromotion promotion = (ImPromotion) searchResult.get(i);
				if (StringUtils.hasText(promotion.getInCharge())) {
					BuEmployeeWithAddressView employeeWithAddressView = buEmployeeWithAddressViewDAO
							.findById(promotion.getInCharge());
					if (employeeWithAddressView != null) {
						promotion.setInChargeName(employeeWithAddressView
								.getChineseName());
					} else {
						promotion.setInChargeName(promotion.getInCharge());
					}
				}
				if (StringUtils.hasText(promotion.getStatus())) {
					promotion.setStatusName(OrderStatus
							.getChineseWord(promotion.getStatus()));
				}
			}
		}
	}

	public Map getSearchSelection(Map parameterMap) throws Exception {

		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			// Object otherBean = parameterMap.get("vatBeanOther");
			// String formName = (String)PropertyUtils.getProperty(otherBean,
			// "formName");
			// String id = (String)PropertyUtils.getProperty(otherBean,
			// "headId");
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,
					AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
					pickerBean, AjaxUtils.SEARCH_KEY);
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			if (result.size() > 0)
				pickerResult.put("result", result);

			// if(formName.equals("GetCombine")){
			// Long headId = NumberUtils.getLong(id);
			// List <ImPromotionReCombine> imCombines = new ArrayList();
			// for( int i=0; i < result.size(); i++){
			// //log.info(NumberUtils.getLong(
			// result.get(i).getProperty("headId")));
			// Long imLineId = NumberUtils.getLong(
			// result.get(i).getProperty("headId") );
			// ImPromotionReCombine imPromotionReCombine =
			// (ImPromotionReCombine)imPromotionReCombineDAO.findByPrimaryKey(
			// ImPromotionReCombine.class, imLineId );
			// imCombines.add(imPromotionReCombine);
			// }
			// saveImPromotionReCombineMod( headId, imCombines );
			// }
			resultMap.put("vatBeanPicker", pickerResult);
			resultMap.put("topLevel", new String[] { "vatBeanPicker" });
			return resultMap;
		} catch (Exception ex) {
			log.error("執行促銷活動檢視失敗，原因：" + ex.toString());
			throw new Exception("執行促銷活動檢視失敗，原因：" + ex.getMessage());
		}
	}

	/**
	 * search PoHead and PoLine Data For FiInvoice Line (T2) for invoice 3.0
	 * Call by ceap savePoDataAction
	 * 
	 * @param findObjs
	 * @return List
	 */
	public void saveImPromotionReCombineMod(Long headId,
			List<ImPromotionReCombine> imCombines) throws Exception {
		if (imCombines != null && imCombines.size() > 0) {
			ImPromotion imPromotion = findById(headId);
			int indexNo = imPromotionReCombineModDAO.findPageLineMaxIndex(
					headId).intValue(); // 取得LINE MAX INDEX
			int customSeq = imPromotionReCombineModDAO.findPageLineMaxIndex(
					headId).intValue(); // 取得LINE MAX
			// customSeq
			for (ImPromotionReCombine imCombine : imCombines) {
				indexNo++;
				ImPromotionReCombineMod imPromotionReCombineMod = new ImPromotionReCombineMod();
				imPromotionReCombineMod.setImPromotion(imPromotion);
				imPromotionReCombineMod.setCombineCode(imCombine
						.getCombineCode());
				imPromotionReCombineMod.setCombineName(imCombine
						.getCombineName());
				imPromotionReCombineMod.setCombineQuantity(imCombine
						.getCombineQuantity());
				imPromotionReCombineMod.setCombinePrice(imCombine
						.getCombinePrice());
				imPromotionReCombineMod.setItemBrand(imCombine.getItemBrand());
				imPromotionReCombineMod
						.setCategory02(imCombine.getCategory02());
				imPromotionReCombineMod.setForeignCategory(imCombine
						.getForeignCategory());
				imPromotionReCombineMod.setUnitPrice(imCombine.getUnitPrice());
				imPromotionReCombineMod.setReCombineId(imCombine.getHeadId());
				imPromotionReCombineMod.setCreatedBy(imCombine.getCreatedBy());
				imPromotionReCombineMod.setCreationDate(new Date());
				imPromotionReCombineMod.setLastUpdatedBy(imCombine
						.getLastUpdatedBy());
				imPromotionReCombineMod.setLastUpdateDate(new Date());
				imPromotionReCombineMod.setIndexNo(Long.valueOf(indexNo));

				imPromotionReCombineModDAO.save(imPromotionReCombineMod);
			}
		}
	}

	/**
	 * 執行促銷活動查詢初始化
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map executeSearchInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String loginBrandCode = (String) PropertyUtils.getProperty(
					otherBean, "loginBrandCode");
			Map multiList = new HashMap(0);
			List<BuOrderType> allOrderTypes = buOrderTypeService
					.findOrderbyType(loginBrandCode, "PM");
			List<ImItemCategory> allItemCategory = imItemCategoryService
					.findByCategoryType(loginBrandCode, "ITEM_CATEGORY");
			multiList.put("allOrderTypes", AjaxUtils.produceSelectorData(
					allOrderTypes, "orderTypeCode", "name", true, false));
			multiList.put("allItemCategory", AjaxUtils
					.produceSelectorData(allItemCategory, "categoryCode",
							"categoryName", true, true));
			resultMap.put("multiList", multiList);

			return resultMap;
		} catch (Exception ex) {
			log.error("促銷活動查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("促銷活動查詢初始化失敗，原因：" + ex.toString());
		}
	}

	public List<Properties> saveSearchResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_MOD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	/**
	 * 執行促銷活動查詢初始化
	 * 
	 * @param parameterMap
	 * @return Map
	 * @throws Exception
	 */
	public Map executeCombineSearchInitial(Map parameterMap) throws Exception {

		HashMap resultMap = new HashMap();
		try {
			return resultMap;
		} catch (Exception ex) {
			log.error("促銷活動查詢初始化失敗，原因：" + ex.toString());
			throw new Exception("促銷活動查詢初始化失敗，原因：" + ex.toString());
		}
	}

	/**
	 * 顯示組合單查詢頁面
	 * 
	 * @param httpRequest
	 * @return List<Properties>
	 * @throws Exception
	 */
	public List<Properties> getAJAXSearchCombinePageData(Properties httpRequest)
			throws Exception {

		try {
			List<Properties> result = new ArrayList();
			List<Properties> gridDatas = new ArrayList();
			int iSPage = AjaxUtils.getStartPage(httpRequest);// 取得起始頁面
			int iPSize = AjaxUtils.getPageSize(httpRequest);// 取得每頁大小
			// ======================帶入查詢的值=========================
			String combineCode = httpRequest.getProperty("combineCode");
			String itemBrand = httpRequest.getProperty("itemBrand");
			String category02 = httpRequest.getProperty("category02");
			String enable = httpRequest.getProperty("enable");
			String enableDate = httpRequest.getProperty("enableDate");
			String foreignCategory = httpRequest.getProperty("foreignCategory");

			Date actualStartDate = null;
			if (StringUtils.hasText(enableDate)) {
				actualStartDate = DateUtils.parseDate(
						DateUtils.C_DATE_PATTON_SLASH, enableDate);
			}
			// =======================執行查詢==============================
			HashMap findObjs = new HashMap();
			findObjs.put("combineCode", combineCode);
			findObjs.put("itemBrand", itemBrand);
			findObjs.put("category02", category02);
			findObjs.put("enable", enable);
			findObjs.put("actualStartDate", actualStartDate);
			findObjs.put("foreignCategory", foreignCategory);

			HashMap promotionMap = imPromotionReCombineDAO.findPageCombineLine(
					findObjs, iSPage, iPSize, BaseDAO.QUERY_SELECT_RANGE);
			List<ImPromotionReCombine> proCombines = (List<ImPromotionReCombine>) promotionMap
					.get(BaseDAO.TABLE_LIST);
			if (proCombines != null && proCombines.size() > 0) {
				Long firstIndex = Long.valueOf(iSPage * iPSize) + 1; // 取得第一筆的INDEX
				promotionMap = imPromotionReCombineDAO.findPageCombineLine(
						findObjs, -1, iPSize, BaseDAO.QUERY_RECORD_COUNT);
				Long maxIndex = (Long) promotionMap
						.get(BaseDAO.TABLE_RECORD_COUNT); // 取得最後一筆 INDEX
				result.add(AjaxUtils.getAJAXPageData(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, proCombines,
						gridDatas, firstIndex, maxIndex));
			} else {
				result.add(AjaxUtils.getAJAXPageDataDefault(httpRequest,
						GRID_SEARCH_FIELD_NAMES,
						GRID_SEARCH_FIELD_DEFAULT_VALUES, gridDatas));
			}

			return result;
		} catch (Exception ex) {
			log.error("載入頁面顯示的促銷活動查詢發生錯誤，原因：" + ex.toString());
			throw new Exception("載入頁面顯示的促銷活動查詢失敗！");
		}
	}

	public List<Properties> saveSearchCombineResult(Properties httpRequest)
			throws Exception {
		String errorMsg = null;
		AjaxUtils.updateSearchResult(httpRequest, GRID_SEARCH_FIELD_NAMES);
		return AjaxUtils.getResponseMsg(errorMsg);
	}

	public Map updateSearchCombineSelection(Map parameterMap) throws Exception {

		Map resultMap = new HashMap(0);
		Map pickerResult = new HashMap(0);
		try {
			Object otherBean = parameterMap.get("vatBeanOther");
			String id = (String) PropertyUtils.getProperty(otherBean, "headId");
			Object pickerBean = parameterMap.get("vatBeanPicker");
			String timeScope = (String) PropertyUtils.getProperty(pickerBean,
					AjaxUtils.TIME_SCOPE);
			ArrayList searchKeys = (ArrayList) PropertyUtils.getProperty(
					pickerBean, AjaxUtils.SEARCH_KEY);
			List<Properties> result = AjaxUtils.getSelectedResults(timeScope,
					searchKeys);
			if (result.size() > 0)
				pickerResult.put("result", result);

			Long headId = NumberUtils.getLong(id);
			List<ImPromotionReCombine> imCombines = new ArrayList();
			for (int i = 0; i < result.size(); i++) {
				log.info(NumberUtils.getLong(result.get(i)
						.getProperty("headId")));
				Long imLineId = NumberUtils.getLong(result.get(i).getProperty(
						"headId"));
				ImPromotionReCombine imPromotionReCombine = (ImPromotionReCombine) imPromotionReCombineDAO
						.findByPrimaryKey(ImPromotionReCombine.class, imLineId);
				imCombines.add(imPromotionReCombine);
			}
			saveImPromotionReCombineMod(headId, imCombines);

			resultMap.put("vatBeanPicker", pickerResult);
			resultMap.put("topLevel", new String[] { "vatBeanPicker" });
			return resultMap;
		} catch (Exception ex) {
			log.error("執行促銷活動檢視失敗，原因：" + ex.toString());
			throw new Exception("執行促銷活動檢視失敗，原因：" + ex.getMessage());
		}
	}
	
	public void updateCombineEable(String brandCode, String actualDataDate, String actualDataDateEnd){
		List<ImPromotionReCombine> imCombines = null;
		imCombines = imPromotionReCombineDAO.findCombineEable(brandCode, actualDataDate, actualDataDateEnd);
		if(imCombines!=null && imCombines.size() > 0){
			for (ImPromotionReCombine imCombine : imCombines) {
				imCombine.setEnable("N");
				imCombine.setLastUpdateDate(new Date());
				imPromotionReCombineDAO.update(imCombine);
			}
		}
	}
	
	/**
     * 更新促銷商品明細
     * 
     * @param headId
     * @throws FormException
     * @throws Exception
     */
    public void saveCombineItemData(Long headId) throws FormException, Exception{
    	try{
    	    ImPromotion promotionPo = imPromotionDAO.findById(headId);
    	    if(promotionPo == null){
    		throw new NoSuchObjectException("查無促銷單主鍵(" + headId + ")的資料！");
    	    }else{
    		String brandCode = promotionPo.getBrandCode();
    		String priceType = promotionPo.getPriceType();    		
    		HashMap conditionMap = new HashMap();
    		conditionMap.put("brandCode", brandCode);
    		conditionMap.put("priceType", priceType);
    		List imPromotionReCombines = promotionPo.getImPromotionReCombines();
    		if(imPromotionReCombines != null && imPromotionReCombines.size() > 0){
    		    for(int i = 0; i < imPromotionReCombines.size(); i++){
    		      ImPromotionReCombineMod imPromotionReCombineMod = (ImPromotionReCombineMod)imPromotionReCombines.get(i);
    			//refreshPromotionItems(conditionMap, promotionItem);
    		      imPromotionReCombineMod.setCreatedBy(promotionPo.getCreatedBy());
    		      imPromotionReCombineMod.setCreationDate(new Date());
    		      imPromotionReCombineMod.setLastUpdatedBy(promotionPo.getCreatedBy());
    		      imPromotionReCombineMod.setLastUpdateDate(new Date());
    		    }
    		    imPromotionDAO.update(promotionPo);
    		}		
    	    }	    
    	}catch (FormException fe) {
    	    log.error("更新組合單明細失敗，原因：" + fe.toString());
    	    throw new FormException(fe.getMessage());
    	} catch (Exception ex) {
    	    log.error("更新組合單明細時發生錯誤，原因：" + ex.toString());
    	    throw new Exception("更新組合單明細時發生錯誤，原因：" + ex.getMessage());
    	}    	
    }
}