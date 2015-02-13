//
//  HealthDataWithUnit.m
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import "HealthDataWithUnit.h"
#import <HealthKit/HealthKit.h>

@implementation HealthDataWithUnit
+ (double)getHealthData:(HKQuantity*)quantity Type:(NSString*)type
{
    HKUnit *unit = nil;
    if ([type isEqualToString:HKQuantityTypeIdentifierBodyMass]) {
        unit = [self poundUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierBodyFatPercentage] || [type isEqualToString:HKQuantityTypeIdentifierOxygenSaturation])
    {
        unit = [self percentUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierRespiratoryRate] || [type isEqualToString:HKQuantityTypeIdentifierHeartRate])
    {
        unit = [self countUnitDevideByUnit:[self minuteUnit]];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierStepCount] || [type isEqualToString:HKQuantityTypeIdentifierFlightsClimbed] || [type isEqualToString:HKQuantityTypeIdentifierNikeFuel] || [type isEqualToString:HKQuantityTypeIdentifierBodyMassIndex])
    {
        unit = [self countUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierDistanceWalkingRunning]
             || [type isEqualToString:HKQuantityTypeIdentifierDistanceCycling])
    {
        unit = [self meterUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierDietaryEnergyConsumed])
    {
        unit = [self calorieUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierActiveEnergyBurned] || [type isEqualToString:HKQuantityTypeIdentifierBasalEnergyBurned])
    {
        unit = [self kilocalorieUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierDietaryBiotin] || [type isEqualToString:HKQuantityTypeIdentifierDietaryChromium] || [type isEqualToString:HKQuantityTypeIdentifierDietaryFolate] || [type isEqualToString:HKQuantityTypeIdentifierDietaryIodine] || [type isEqualToString:HKQuantityTypeIdentifierDietaryMolybdenum] || [type isEqualToString:HKQuantityTypeIdentifierDietarySelenium] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminA] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminB12] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminD] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminK])
    {
        unit = [self gramPrefixUnit:HKMetricPrefixMicro];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierDietaryCaffeine] || [type isEqualToString:HKQuantityTypeIdentifierDietaryCalcium] || [type isEqualToString:HKQuantityTypeIdentifierDietaryChloride] || [type isEqualToString:HKQuantityTypeIdentifierDietaryCopper] || [type isEqualToString:HKQuantityTypeIdentifierDietaryIron] || [type isEqualToString:HKQuantityTypeIdentifierDietaryMagnesium] || [type isEqualToString:HKQuantityTypeIdentifierDietaryManganese] || [type isEqualToString:HKQuantityTypeIdentifierDietaryNiacin] || [type isEqualToString:HKQuantityTypeIdentifierDietaryPantothenicAcid] || [type isEqualToString:HKQuantityTypeIdentifierDietaryPhosphorus] || [type isEqualToString:HKQuantityTypeIdentifierDietaryPotassium] || [type isEqualToString:HKQuantityTypeIdentifierDietaryRiboflavin] || [type isEqualToString:HKQuantityTypeIdentifierDietarySodium] || [type isEqualToString:HKQuantityTypeIdentifierDietaryThiamin] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminB6] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminC] || [type isEqualToString:HKQuantityTypeIdentifierDietaryVitaminE] || [type isEqualToString:HKQuantityTypeIdentifierDietaryZinc])
    {
        unit = [self gramPrefixUnit:HKMetricPrefixMilli];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierDietaryCarbohydrates] || [type isEqualToString:HKQuantityTypeIdentifierDietaryFatMonounsaturated] || [type isEqualToString:HKQuantityTypeIdentifierDietaryFatPolyunsaturated] || [type isEqualToString:HKQuantityTypeIdentifierDietaryFatSaturated] || [type isEqualToString:HKQuantityTypeIdentifierDietaryFatTotal] || [type isEqualToString:HKQuantityTypeIdentifierDietaryFiber] || [type isEqualToString:HKQuantityTypeIdentifierDietaryProtein] || [type isEqualToString:HKQuantityTypeIdentifierDietarySugar] || [type isEqualToString:HKQuantityTypeIdentifierDietaryCholesterol])
    {
        unit = [self gramUnit];
    }
    else if ([type isEqualToString:HKQuantityTypeIdentifierBloodPressureSystolic] || [type isEqualToString:HKQuantityTypeIdentifierBloodPressureDiastolic])
    {
        unit = [self pascalUnit];
    }
    
    double usersData = [quantity doubleValueForUnit:unit];
    return usersData;
}

//length
+ (HKUnit*)meterUnit
{
    HKUnit *meterUnit = [HKUnit meterUnit];
    return meterUnit;
}

+ (HKUnit*)meterPrefixUnit:(HKMetricPrefix)prefix
{
    HKUnit *meterPrefixUnit = [HKUnit meterUnitWithMetricPrefix:prefix];
    return meterPrefixUnit;
}

+ (HKUnit*)inchUnit
{
    HKUnit *inchUnit = [HKUnit inchUnit];
    return inchUnit;
}

+ (HKUnit*)footUnit
{
    HKUnit *footUnit = [HKUnit footUnit];
    return footUnit;
}

+ (HKUnit*)mileUnit
{
    HKUnit *mileUnit = [HKUnit mileUnit];
    return mileUnit;
}


//mass
+ (HKUnit*)gramUnit
{
    HKUnit *gramUnit = [HKUnit gramUnit];
    return gramUnit;
}

+ (HKUnit*)gramPrefixUnit:(HKMetricPrefix)prefix
{
    HKUnit *gramPrefixUnit = [HKUnit gramUnitWithMetricPrefix:prefix];
    return gramPrefixUnit;
}

+ (HKUnit*)ounceUnit
{
    HKUnit *ounceUnit = [HKUnit ounceUnit];
    return ounceUnit;
}

+ (HKUnit*)poundUnit
{
    HKUnit *poundUnit = [HKUnit poundUnit];
    return poundUnit;
}

+ (HKUnit*)stoneUnit
{
    HKUnit *stoneUnit = [HKUnit stoneUnit];
    return stoneUnit;
}

+ (HKUnit*)moleUnit:(double)molarMass
{
    HKUnit *moleUnit = [HKUnit moleUnitWithMolarMass:molarMass];
    return moleUnit;
}

+ (HKUnit*)molePrefixunit:(HKMetricPrefix)prefix MolarMass:(double)molarMass
{
    HKUnit *molePrefixUnit = [HKUnit moleUnitWithMetricPrefix:prefix molarMass:molarMass];
    return molePrefixUnit;
}

//volume
+ (HKUnit*)literUnit
{
    HKUnit *literUnit = [HKUnit literUnit];
    return literUnit;
}

//pressure
+ (HKUnit*)pascalUnit
{
    HKUnit *pascaUnit = [HKUnit pascalUnit];
    return pascaUnit;
}

//time
+ (HKUnit*)secondUnit
{
    HKUnit *secondUnit = [HKUnit secondUnit];
    return secondUnit;
}

+ (HKUnit*)minuteUnit
{
    HKUnit *minuteUnit = [HKUnit minuteUnit];
    return minuteUnit;
}

//energy
+ (HKUnit*)jouleUnit
{
    HKUnit *jouleUnit = [HKUnit jouleUnit];
    return jouleUnit;
}

+ (HKUnit*)calorieUnit
{
    HKUnit *calorieUnit = [HKUnit calorieUnit];
    return calorieUnit;
}

+ (HKUnit*)kilocalorieUnit
{
    HKUnit *kilocalorieUnit = [HKUnit kilocalorieUnit];
    return kilocalorieUnit;
}

//temperature
+ (HKUnit*)degreeCelsiusUnit
{
    HKUnit *degreeCelsiusUnit = [HKUnit degreeCelsiusUnit];
    return degreeCelsiusUnit;
}

//electrical
+ (HKUnit*)siemenUnit
{
    HKUnit *siemenUnit = [HKUnit siemenUnit];
    return siemenUnit;
}

//scalar
+ (HKUnit*)percentUnit
{
    HKUnit *percentUnit = [HKUnit percentUnit];
    return percentUnit;
}

+ (HKUnit*)countUnit
{
    HKUnit *countUnit = [HKUnit countUnit];
    return countUnit;
}

+ (HKUnit*)countUnitDevideByUnit:(HKUnit*)unit
{
    HKUnit *countUnit = [[HKUnit countUnit] unitDividedByUnit:unit];
    return countUnit;
}

@end
