//
//  AccHealthDataRegister.h
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <HealthKit/HealthKit.h>
#import "AccHealthDataCollector.h"

@interface AccHealthDataRegister : NSObject<AccHealthDataCollectorDelegate>

@property (nonatomic) HKHealthStore *healthStore;

- (instancetype)initWithHealthStore:(HKHealthStore*)store;
- (void)registAlldata:(id<AccHealthDataCollectorDelegate>)delegate;
@end
