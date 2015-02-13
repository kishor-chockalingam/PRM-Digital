//
//  NSSet+HealthKitPermission.m
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import "NSSet+HealthKitPermission.h"
#import <HealthKit/HealthKit.h>

@implementation NSSet (HealthKitPermission)

+ (NSSet*)setReadPermission
{
    HKQuantityType *weightType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierBodyMass];//kg
    HKQuantityType *BMIType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierBodyMassIndex];//kg
    HKQuantityType *fatPercentageType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierBodyFatPercentage];//%
    HKQuantityType *heartRateType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierHeartRate];//times
    HKQuantityType *bloodPressureSystolicType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierBloodPressureSystolic];//count
    HKQuantityType *bloodPressureDiastolicType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierBloodPressureDiastolic];//count
    HKQuantityType *respiratoryRateType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierRespiratoryRate];//times/min
    HKQuantityType *oxygenStaturationType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierOxygenSaturation];//%
    
    //Active
    HKQuantityType *stepCountType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierStepCount];
    HKQuantityType *distanceWARType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDistanceWalkingRunning];
    HKQuantityType *distanceCyclingType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDistanceCycling];
    HKQuantityType *basalEnergyBurnedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierBasalEnergyBurned];
    HKQuantityType *activeEnergyBurnedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierActiveEnergyBurned];
    HKQuantityType *flightsClimbedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierFlightsClimbed];
    HKQuantityType *nikeFuelType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierNikeFuel];
    
    //Nutrition
    HKQuantityType *biotinType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryBiotin];
    HKQuantityType *caffeineType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryCaffeine];
    HKQuantityType *calciumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryCalcium];
    HKQuantityType *carbohydratesType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryCarbohydrates];
    HKQuantityType *chlorideType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryChloride];
    HKQuantityType *cholesterolType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryCholesterol];
    HKQuantityType *chromiumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryChromium];
    HKQuantityType *copperType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryCopper];
    HKQuantityType *energyConsumedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryEnergyConsumed];
    HKQuantityType *fatMonounsaturatedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryFatMonounsaturated];
    HKQuantityType *fatPolyunsaturatedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryFatPolyunsaturated];
    HKQuantityType *fatSaturatedType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryFatSaturated];
    HKQuantityType *fatTotalType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryFatTotal];
    HKQuantityType *fiberType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryFiber];
    HKQuantityType *folateType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryFolate];
    HKQuantityType *iodineType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryIodine];
    HKQuantityType *ironType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryIron];
    HKQuantityType *magnesiumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryMagnesium];
    HKQuantityType *manganeseType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryManganese];
    HKQuantityType *molybdenumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryMolybdenum];
    HKQuantityType *niacinType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryNiacin];
    HKQuantityType *pantothenicAcidType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryPantothenicAcid];
    HKQuantityType *phosphorusType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryPhosphorus];
    HKQuantityType *potassiumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryPotassium];
    HKQuantityType *proteinType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryProtein];
    HKQuantityType *riboflavinType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryRiboflavin];
    HKQuantityType *seleniumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietarySelenium];
    HKQuantityType *sodiumType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietarySodium];
    HKQuantityType *sugarType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietarySugar];
    HKQuantityType *thiaminType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryThiamin];
    HKQuantityType *vitaminAType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminA];
    HKQuantityType *vitaminB12Type = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminB12];
    HKQuantityType *vitaminB6Type = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminB6];
    HKQuantityType *vitaminCType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminC];
    HKQuantityType *vitaminDType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminD];
    HKQuantityType *vitaminEType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminE];
    HKQuantityType *vitaminKType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryVitaminK];
    HKQuantityType *zincType = [HKObjectType quantityTypeForIdentifier:HKQuantityTypeIdentifierDietaryZinc];
    
    //sleep
    HKCategoryType *sleepAnalystType = [HKObjectType categoryTypeForIdentifier:HKCategoryTypeIdentifierSleepAnalysis];
    
    
    return [self setWithObjects:weightType, BMIType, fatPercentageType, heartRateType, bloodPressureDiastolicType, bloodPressureSystolicType, respiratoryRateType, oxygenStaturationType, sleepAnalystType, stepCountType, distanceWARType, distanceCyclingType, basalEnergyBurnedType, activeEnergyBurnedType, flightsClimbedType, nikeFuelType, biotinType, caffeineType, calciumType, carbohydratesType, carbohydratesType, chlorideType, cholesterolType, chromiumType, copperType, energyConsumedType, fatMonounsaturatedType, fatPolyunsaturatedType, fatSaturatedType, fatTotalType, fiberType, folateType, iodineType, ironType, magnesiumType, manganeseType, molybdenumType, niacinType, pantothenicAcidType, phosphorusType, potassiumType, proteinType, riboflavinType, seleniumType, sodiumType, sugarType, thiaminType, vitaminAType, vitaminB12Type, vitaminB6Type, vitaminCType, vitaminDType, vitaminEType, vitaminKType, zincType, sleepAnalystType, nil];
}

+ (NSSet*)setWritePermission
{
    return [self setWithObjects:nil];
}
@end
