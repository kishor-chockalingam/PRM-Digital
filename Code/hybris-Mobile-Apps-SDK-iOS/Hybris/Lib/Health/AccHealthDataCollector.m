//
//  AccHealthDataCollector.m
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import "AccHealthDataCollector.h"
#import "HKHealthStore+SampleType.h"
#import "HealthDataWithUnit.h"
#import "AccHealthDataOuter.h"

@implementation AccHealthDataCollector
@synthesize registerArray, delegate, healthStore;

#pragma mark - get data
- (instancetype)init
{
    if (self = [super init])
    {
        [[AccHealthDataOuter shareInstance] clearData];
    }
    return self;
}

- (void)getDataFromType:(NSString*)type
{
    HKQuantityType *quantityType = [HKQuantityType quantityTypeForIdentifier:type];
    [self.healthStore QuantitySampleOfType:quantityType predicate:nil completion:^(HKQuantity *mostRecentQuantity, NSError *error) {
        if (!mostRecentQuantity) {
            NSLog(@"Either an error occured fetching the user's information or none has been stored yet. In your app, try to handle this gracefully.");
        }
        else
        {
//            NSLog(@"%@", type);
            double userData = [HealthDataWithUnit getHealthData:mostRecentQuantity Type:type];
            NSString *strData = [NSString stringWithFormat:@"%f", userData];
//            NSLog(@"%@", strData);
            NSDictionary *dictionary = [NSDictionary dictionaryWithObjectsAndKeys:type, @"Type", strData, @"Value", nil];
            [[AccHealthDataOuter shareInstance] addData:dictionary];
        }
        [self cancelRegistData:type];
        if ([self FinishReading])
        {
            [delegate FinishCollect];
        }
    }];
}

- (void)getDataFromType:(NSString*)type completion:(void (^)(NSDictionary *, NSError *))completion
{
    HKQuantityType *quantityType = [HKQuantityType quantityTypeForIdentifier:type];
    [self.healthStore QuantitySampleOfType:quantityType predicate:nil completion:^(HKQuantity *mostRecentQuantity, NSError *error) {
        if (!mostRecentQuantity) {
            NSLog(@"Either an error occured fetching the user's information or none has been stored yet. In your app, try to handle this gracefully.");
            if (completion)
            {
                completion(nil, error);
            }
        }
        else
        {
//            NSLog(@"%@", type);
            double userData = [HealthDataWithUnit getHealthData:mostRecentQuantity Type:type];
            NSString *strData = [NSString stringWithFormat:@"%f", userData];
//            NSLog(@"%@", strData);
            NSDictionary *dictionary = [NSDictionary dictionaryWithObjectsAndKeys:type, @"Type", strData, @"Value", nil];
            completion(dictionary, error);
        }
        
    }];
}

//read category type
- (void)categorySampleOfType
{
    HKCategoryType *categoryType = [HKObjectType categoryTypeForIdentifier:HKCategoryTypeIdentifierSleepAnalysis];
    
    HKCategorySample *categorySample =
    [HKCategorySample categorySampleWithType:categoryType
                                       value:HKCategoryValueSleepAnalysisAsleep
                                   startDate:[NSDate date]
                                     endDate:[NSDate dateWithTimeIntervalSinceNow:1000000]];
    NSInteger count = categorySample.value;
    NSString *strCount = [NSString stringWithFormat:@"%ld", (long)count];
    NSDictionary *dictionary = [NSDictionary dictionaryWithObjectsAndKeys:HKCategoryTypeIdentifierSleepAnalysis, @"Type", strCount, @"Value", nil];
    [[AccHealthDataOuter shareInstance] addData:dictionary];
}

#pragma mark - register data
- (void)registAllData
{
    if (self.registerArray == nil)
    {
        registerArray = [[NSMutableArray alloc] init];
    }
    [registerArray addObject:HKQuantityTypeIdentifierBodyMass];
    [registerArray addObject:HKQuantityTypeIdentifierBodyMassIndex];
    [registerArray addObject:HKQuantityTypeIdentifierBodyFatPercentage];
    [registerArray addObject:HKQuantityTypeIdentifierHeartRate];
    [registerArray addObject:HKQuantityTypeIdentifierBloodPressureSystolic];
    [registerArray addObject:HKQuantityTypeIdentifierBloodPressureDiastolic];
    [registerArray addObject:HKQuantityTypeIdentifierRespiratoryRate];
    [registerArray addObject:HKQuantityTypeIdentifierOxygenSaturation];
    
    [registerArray addObject:HKQuantityTypeIdentifierStepCount];
    [registerArray addObject:HKQuantityTypeIdentifierDistanceWalkingRunning];
    [registerArray addObject:HKQuantityTypeIdentifierDistanceCycling];
    [registerArray addObject:HKQuantityTypeIdentifierBasalEnergyBurned];
    [registerArray addObject:HKQuantityTypeIdentifierActiveEnergyBurned];
    [registerArray addObject:HKQuantityTypeIdentifierFlightsClimbed];
    [registerArray addObject:HKQuantityTypeIdentifierNikeFuel];
    
    [registerArray addObject:HKQuantityTypeIdentifierDietaryBiotin];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryCaffeine];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryCalcium];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryCarbohydrates];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryChloride];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryCholesterol];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryChromium];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryCopper];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryEnergyConsumed];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryFatMonounsaturated];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryFatPolyunsaturated];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryFatSaturated];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryFatTotal];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryFiber];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryFolate];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryIodine];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryIron];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryMagnesium];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryManganese];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryMolybdenum];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryNiacin];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryPantothenicAcid];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryPhosphorus];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryPotassium];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryProtein];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryRiboflavin];
    [registerArray addObject:HKQuantityTypeIdentifierDietarySelenium];
    [registerArray addObject:HKQuantityTypeIdentifierDietarySodium];
    [registerArray addObject:HKQuantityTypeIdentifierDietarySugar];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryThiamin];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminA];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminB12];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminB6];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminC];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminD];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminE];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryVitaminK];
    [registerArray addObject:HKQuantityTypeIdentifierDietaryZinc];
}

- (void)cancelRegistData:(NSString*)key
{
    if (self.registerArray != nil)
    {
        [registerArray removeObject:key];
    }
}

- (BOOL)FinishReading
{
    if ([self.registerArray count] > 0)
    {
        return NO;
    }
    else
    {
        return YES;
    }
}

- (void)startObserver:(id)sender
{
    [sender addObserver:self forKeyPath:@"Register" options:NSKeyValueObservingOptionNew|NSKeyValueObservingOptionOld context:nil];
}

- (void)readAllRequiredType
{
    [self getDataFromType:HKQuantityTypeIdentifierBodyMass];
    [self getDataFromType:HKQuantityTypeIdentifierBodyMassIndex];
    [self getDataFromType:HKQuantityTypeIdentifierBodyFatPercentage];
    [self getDataFromType:HKQuantityTypeIdentifierHeartRate];
    [self getDataFromType:HKQuantityTypeIdentifierBloodPressureSystolic];
    [self getDataFromType:HKQuantityTypeIdentifierBloodPressureDiastolic];
    [self getDataFromType:HKQuantityTypeIdentifierRespiratoryRate];
    [self getDataFromType:HKQuantityTypeIdentifierOxygenSaturation];
    
    [self getDataFromType:HKQuantityTypeIdentifierStepCount];
    [self getDataFromType:HKQuantityTypeIdentifierDistanceWalkingRunning];
    [self getDataFromType:HKQuantityTypeIdentifierDistanceCycling];
    [self getDataFromType:HKQuantityTypeIdentifierBasalEnergyBurned];
    [self getDataFromType:HKQuantityTypeIdentifierActiveEnergyBurned];
    [self getDataFromType:HKQuantityTypeIdentifierFlightsClimbed];
    [self getDataFromType:HKQuantityTypeIdentifierNikeFuel];
    
    [self getDataFromType:HKQuantityTypeIdentifierDietaryBiotin];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryCaffeine];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryCalcium];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryCarbohydrates];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryChloride];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryCholesterol];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryChromium];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryCopper];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryEnergyConsumed];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryFatMonounsaturated];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryFatPolyunsaturated];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryFatSaturated];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryFatTotal];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryFiber];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryFolate];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryIodine];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryIron];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryMagnesium];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryManganese];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryMolybdenum];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryNiacin];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryPantothenicAcid];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryPhosphorus];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryPotassium];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryProtein];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryRiboflavin];
    [self getDataFromType:HKQuantityTypeIdentifierDietarySelenium];
    [self getDataFromType:HKQuantityTypeIdentifierDietarySodium];
    [self getDataFromType:HKQuantityTypeIdentifierDietarySugar];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryThiamin];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminA];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminB12];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminB6];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminC];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminD];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminE];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryVitaminK];
    [self getDataFromType:HKQuantityTypeIdentifierDietaryZinc];
    [self categorySampleOfType];
}

@end
