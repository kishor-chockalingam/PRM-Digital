//
//  HealthDataWithUnit.h
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <HealthKit/HealthKit.h>

@interface HealthDataWithUnit : NSObject

+ (double)getHealthData:(HKQuantity*)quantity Type:(NSString*)type;
@end
