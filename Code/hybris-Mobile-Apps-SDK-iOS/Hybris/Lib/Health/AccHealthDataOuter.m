//
//  AccHealthDataOuter.m
//  HealthkitTest
//
//  Created by CDC on 2/2/15.
//  Copyright (c) 2015 Accenture. All rights reserved.
//

#import "AccHealthDataOuter.h"

@implementation AccHealthDataOuter
@synthesize dataArrays;
- (instancetype)init
{
    if (self = [super init])
    {
        dataArrays = [[NSMutableArray alloc] init];
    }
    return self;
}

- (void)clearData
{
    [self.dataArrays removeAllObjects];
}

- (void)addData:(NSDictionary*)dictionary
{
    [self.dataArrays addObject:dictionary];
}

+ (AccHealthDataOuter*)shareInstance
{
    static AccHealthDataOuter *shareInstance;
    
    @synchronized(self)
    {
        if (!shareInstance)
            shareInstance = [[AccHealthDataOuter alloc] init];
        
        return shareInstance;
    }
}


@end
