//
//  AccHealthDataOuter.h
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface AccHealthDataOuter : NSObject
@property (strong, nonatomic) NSMutableArray *dataArrays;

- (void)clearData;
- (void)addData:(NSDictionary*)dictionary;

+ (AccHealthDataOuter*)shareInstance;
@end
